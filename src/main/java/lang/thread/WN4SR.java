package lang.thread;

public class WN4SR {
    private final static Object object = new Object();
    public static class ChangeObjectThread extends Thread {
        public ChangeObjectThread(String name) {
            super.setName(name);
        }
        volatile boolean suspendMe = false;
        public void suspendMe() {
            this.suspendMe = true;
        }
        public void resumeMe() {
            this.suspendMe = false;
            synchronized (this) {
                this.notify();
            }
        }
        @Override
        public void run() {
            while (true) {
                synchronized (this) {
                    if (suspendMe) {
                        try {
                            System.out.println(getName() + " suspend");
                            this.wait();
                            System.out.println(getName() + " resume");
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                synchronized (object) {
                    System.out.println("in ChangeObjectThread");
                }
                Thread.yield();
            }
        }
    }
    public static class ReadObjectThread extends Thread {
        @Override
        public void run() {
            while (true) {
                synchronized (object) {
                    System.out.println("in ReadObjectThread");
                }
                Thread.yield();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ChangeObjectThread t1 = new ChangeObjectThread("change object thread");
        ReadObjectThread t2 = new ReadObjectThread();
        t1.start();
        t2.start();
        Thread.sleep(1000);
        t1.suspendMe();
        System.out.println("t1 suspend 2 sec");
        Thread.sleep(2000);
        System.out.println("resume t1");
        t1.resumeMe();
    }
}
