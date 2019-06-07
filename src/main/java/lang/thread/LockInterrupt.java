package lang.thread;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 重入锁相应中断
 */
public class LockInterrupt implements Runnable {
    public static ReentrantLock reentrantLock1 = new ReentrantLock();
    public static ReentrantLock reentrantLock2 = new ReentrantLock();
    int lock;
    public LockInterrupt(int i) {
        this.lock = i;
    }
    @Override
    public void run() {
        try {
            if (lock == 1) {
                reentrantLock1.lockInterruptibly();
                try {
                    Thread.sleep(500);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    //reentrantLock1.unlock();
                }
                reentrantLock2.lockInterruptibly();
            }
            else {
                if (lock == 2) {
                    reentrantLock2.lockInterruptibly();
                    try {
                        Thread.sleep(500);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finally {
                        //reentrantLock2.unlock();
                    }
                    reentrantLock1.lockInterruptibly();
                }
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            if (reentrantLock1.isHeldByCurrentThread()) {
                reentrantLock1.unlock();
            }
            if (reentrantLock2.isHeldByCurrentThread()) {
                reentrantLock2.unlock();
            }
            System.out.println(Thread.currentThread().getId() + "线程退出");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LockInterrupt lockInterrupt = new LockInterrupt(1);
        LockInterrupt lockInterrupt1 = new LockInterrupt(2);
        Thread t1 = new Thread(lockInterrupt);
        Thread t2 = new Thread(lockInterrupt1);
        t1.start();
        t2.start();

        Thread.sleep(1000);
        t2.interrupt();
    }
}
