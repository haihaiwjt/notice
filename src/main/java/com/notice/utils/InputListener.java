package src.main.java.com.notice.utils;

import src.main.java.com.notice.model.Operation;
import src.main.java.com.notice.model.Task;
import javax.swing.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
import java.util.regex.Pattern;

import static src.main.java.com.notice.utils.Notice.queue;

// 多线程接受控制台输入
public class InputListener implements Runnable {
    private final Notice notice;

    public InputListener(Notice notice) {
        this.notice = notice;
    }

        public void run() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("输入闹钟时间：(格式为add HH mm ss): \n输入“exit”或“EXIT”退出程序,“list”展示闹钟列表,“help”给出帮助\n输入“delete HH:mm:ss”来删除闹钟,“change HH mm ss to HH mm ss”来修改闹钟");

            while (!Notice.needExit) {
                try {// 读取用户输入并放入队列（非阻塞式，因为队列有容量限制时会阻塞）
                    // 转换为localtime格式
                    String inputString = scanner.nextLine();
                    if(inputString.isEmpty()) continue;
                    Operation operation = InputMatch.matchOperation(inputString);
                switch(operation){

                    // 先看是否为退出信号
                    case EXIT -> {
                        if(notice.exitService()) {
                            scanner.close();
                            continue;
                        }
                        throw new RuntimeException("退出出错，叫程序员debug吧");}

                    case LIST -> {
                        if(notice.list()) continue;
                        throw new RuntimeException("没能给出闹钟列表真是抱歉啊");
                        }

                    case DELETENOTICE -> {
//                        System.out.println("输入要删除的闹钟时间，格式为HH:mm:ss");
                        String deleteInput = CommonUtil.changeToTimePattern(inputString);
                        LocalTime deleteTime = CommonUtil.stringTimeToLocalTime(deleteInput);
                        deleteTime = deleteTime.truncatedTo(ChronoUnit.SECONDS);
                        if(notice.delete(deleteTime)) {
                            notice.addNewNotice();
                            continue;
                        }
                        throw new RuntimeException("删除闹钟"+ deleteTime.toString() +"出错，叫程序员检查吧");}

                    case ADDNOTICE -> {
                        // 转换input为HH:mm:ss格式
                        String input = CommonUtil.changeToTimePattern(inputString);
                        LocalTime tryTurnTime = CommonUtil.stringTimeToLocalTime(input);
                        // 将时间放入队列
                        Task newTask = setTask(tryTurnTime);
                        System.out.println("已将闹钟"+ input +"放入队列");
                        queue.put(newTask);
                        // 此处要加上唤醒trigger线程的情况
                        notice.addNewNotice();}

                    case HELP -> {notice.help();}

                    case CHANGENOTICE -> {

                        String[] times = CommonUtil.findChangeTime(inputString);
                        if(notice.change(times)) {
                            notice.addNewNotice();
                            continue;
                        }
                        throw new RuntimeException("没有该闹钟捏，要不先用list查看一下？");
                    }

                    case UNKNOWNOPERATION -> {
                        System.out.println("输入格式错误，请重新输入");
                        }
                    }

                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
//            System.out.println("输入线程结束");
        }

        private Task setTask(LocalTime time) {
            LocalTime tempTime = time.truncatedTo(ChronoUnit.SECONDS);
            return new Task(() -> {
                // 图形化弹窗展示
                String message = "到时间啦，阿米娅提醒博士要去休息了呢，劳逸结合才能更好学习呀";
                SwingUtilities.invokeLater(new PopPrompt(message));
//                System.out.println("请输入下一个时间或输入exit退出：");
            },tempTime);
        }

}
