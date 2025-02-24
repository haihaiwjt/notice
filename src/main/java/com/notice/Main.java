package src.main.java.com.notice;

import src.main.java.com.notice.utils.InputListener;
import src.main.java.com.notice.utils.Notice;



public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("now start Notice...");
        Notice notice = new Notice();

        Thread inPutThread = new Thread(new InputListener(notice));
        inPutThread.start();
        notice.startService();
        while(!Notice.needExit){
            Thread.sleep(1000);
        }
        notice.stopService();
        System.out.println("exit Notice");
        System.exit(0);
    }
}
