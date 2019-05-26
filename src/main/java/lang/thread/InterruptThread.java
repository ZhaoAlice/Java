package lang.thread;

/**
 * three methods:
 * Thread.interrupted()
 * Thread.isInterrupted()
 * Thread.interrupt()
 */
public class InterruptThread {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Interrupted");
                        break;
                    }
                }
                Thread.yield();
            }
        };
        t.start();
        Thread.sleep(1000);
        t.interrupt();
        Thread t1 = new Thread("sleep interrupt") {
            @Override
            public void run() {
                while (true) {
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println(getName() + "interrupted");
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                        Thread.currentThread().interrupt();
                    }
                    Thread.yield();
                }
            }
        };
        t1.start();
        Thread.sleep(100);
        t1.interrupt();
    }
}
