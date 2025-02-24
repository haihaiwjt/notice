package src.main.java.com.notice.utils;


import src.main.java.com.notice.model.Task;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class Notice {
    public ExecutorService executorService = Executors.newFixedThreadPool(5);
    public static PriorityBlockingQueue<Task> queue = new PriorityBlockingQueue<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition conditionAll = lock.newCondition();
    private final Condition conditionSingle = lock.newCondition();
    public volatile static boolean needExit = false;
    Trigger trigger = new Trigger();

    //用来将任务交给并唤醒触发器。
    public void startService() throws InterruptedException {
        Thread triggerThread = new Thread(() ->trigger.run());
        triggerThread.start();
    }

    //添加闹钟时唤醒
    public void addNewNotice() throws InterruptedException {
        trigger.wakeUpSingle();
        trigger.wakeUpAll();
    }
    //退出闹钟
    public Boolean exitService(){
        queue.clear();
        needExit = true;
        trigger.wakeUpSingle();
        trigger.wakeUpAll();
        return true;
    }
    public void stopService(){
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
    //删除闹钟
    public Boolean delete(LocalTime setTime){
        final int[] i = {0};
        if(!queue.isEmpty()){
            queue.forEach(element -> {
                if(element != null && element.getSetTime().equals(setTime) && i[0] == 0){
                    queue.remove(element);
                    i[0]++;
                    System.out.println("已删除闹钟" + setTime);
                }
                    });

            return i[0] > 0;
        }
        System.out.println("队列中没有该闹钟，请使用list命令查看后删除");
        return false;
    }
    //展示闹钟列表
    public Boolean list(){
        if(queue.isEmpty()) return false;
        System.out.println("闹钟列表如下：");
        queue.forEach(element ->{
            System.out.println(element.getSetTime().toString());
        });
        return true;
    }
    //帮助
    public void help(){
        System.out.println("输入闹钟时间：(格式为add HH mm ss): \n输入“exit”或“EXIT”退出程序,“list”展示闹钟列表,“help”给出帮助\n输入“delete HH:mm:ss”来删除闹钟,“change HH mm ss to HH mm ss”来修改闹钟");
    }
    //修改闹钟时间
    public Boolean change(String[] strings){
        int[] i = {0};
        String oldStringTime = strings[0];
        String newStringTime = strings[1];
        LocalTime oldTime = CommonUtil.stringTimeToLocalTime(oldStringTime);
        LocalTime newTime = CommonUtil.stringTimeToLocalTime(newStringTime);
        queue.forEach(element ->{
            if(element.getSetTime().equals(oldTime) && i[0] == 0){
                element.setSetTime(newTime);
                System.out.println("修改闹钟"+ oldTime +"成功");
                i[0]++;
            }
        });
        return i[0] > 0;
    }

    public Trigger getTrigger(){
        return this.trigger;
    }

    //等待合适的时间，到后将任务交给线程池
    public class Trigger {

        public void run() {
//            System.out.println("启动了触发器");

            while(!needExit) {
                lock.lock();
                try {
                    if (queue.isEmpty()) {
                        conditionAll.await();
                    }
                    if (queue.isEmpty()) {
//                        System.out.println("准备退出trigger线程");
                        continue;
                    }

                    Task lateTask = queue.peek();
                    LocalTime nowTruncated = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
                    LocalTime setTimeTruncated = lateTask.getSetTime().truncatedTo(ChronoUnit.SECONDS);
                    long waitTime = ChronoUnit.SECONDS.between(nowTruncated, setTimeTruncated);

                    if (waitTime < 0) {
                        System.out.println("已经是过去的时间啦，阿米娅再也不想让博士回想起过去的悲伤了");
                        queue.poll();
                        continue;
                    }
                    //时间到了，执行任务
                    if (waitTime == 0) {
                        lateTask = queue.poll();
                        if (lateTask != null) {
                            executorService.execute(lateTask.getJob());
                        }
                    } else {
                        //等待对应时间
                        System.out.println("下一个闹钟要等待" + waitTime + "秒\n请输入新的时间或exit退出程序");
                        conditionSingle.await(waitTime, TimeUnit.SECONDS);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }finally {
                    lock.unlock();
                }
            }
//            System.out.println("trigger线程结束");
        }

        void wakeUpSingle(){
            lock.lock();
            try{
                conditionSingle.signalAll();
            } finally{
                lock.unlock();
            }
        }
        void wakeUpAll(){
            lock.lock();
            try{
                conditionAll.signalAll();
            }finally{
                lock.unlock();
            }
        }
    }
}
