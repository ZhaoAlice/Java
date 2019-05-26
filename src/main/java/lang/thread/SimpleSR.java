package lang.thread;

public class SimpleSR {
    private final static Object object = new Object();
    private static ChangeObjectThread changeObjectThread1 = new ChangeObjectThread("t1");
    private static ChangeObjectThread changeObjectThread2 = new ChangeObjectThread("t2");
    public static class ChangeObjectThread extends Thread {
        public ChangeObjectThread(String name) {
            super.setName(name);
        }
        @Override
        public void run() {
            synchronized (object) {
                System.out.println("enter" + getName());
                Thread.currentThread().suspend();
                System.out.println(getName() + "end");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(System.currentTimeMillis());
        changeObjectThread1.start();
        Thread.sleep(1000);
        System.out.println(System.currentTimeMillis());
        changeObjectThread2.start();
        changeObjectThread1.resume();
        changeObjectThread2.resume();
        changeObjectThread2.join();
        changeObjectThread1.join();
    }
}
