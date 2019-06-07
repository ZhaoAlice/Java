package lang.thread;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 重入锁-simple example
 */
public class LockT implements Runnable {
    private static ReentrantLock lock = new ReentrantLock();
    private static int i = 0;
    @Override
    public void run() {
        for (int j = 0; j < 100000; j++) {
            lock.lock();
            try {
                i++;
            }
            finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LockT lockT = new LockT();
        Thread t1 = new Thread(lockT);
        Thread t2 = new Thread(lockT);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }
}
