package lang.thread;

public class SimpleWN {
    private final static Object object = new Object();
    public static class WT extends Thread {
        @Override
        public void run() {
            synchronized (object) {
                System.out.println(System.currentTimeMillis() + ": TW start");
                try {
                    System.out.println(System.currentTimeMillis() + ": TW wait for object");
                    object.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(System.currentTimeMillis() + ": TW end");
            }
        }
    }
    public static class NT extends Thread {
        @Override
        public void run() {
            synchronized (object) {
                System.out.println(System.currentTimeMillis() + ": TN notify object");
                object.notify();
                System.out.println(System.currentTimeMillis() + ": TN end");
                try {
                    // 休眠两秒，WT线程需要object对象监视器，才能继续执行
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new WT().start();
        new NT().start();
    }
}
