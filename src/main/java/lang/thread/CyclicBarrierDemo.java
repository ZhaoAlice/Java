package lang.thread;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

class CyclicBarrierDemo {
    public static class Soldier implements Runnable {
        private String soldier;
        private final CyclicBarrier cyclic;
        Soldier(CyclicBarrier cyclicBarrier, String soldierName) {
            this.cyclic = cyclicBarrier;
            this.soldier = soldierName;
        }

        @Override
        public void run() {
            try {
                // 集齐初始化数量的线程后，执行run方法,同时将计数重置为0
                cyclic.await();
                doWork();
                // 再次集齐初始化数量的线程后，执行run方法,同时将计数重置为0
                cyclic.await();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
        private void doWork() {
            try {
                Thread.sleep(Math.abs(new Random().nextInt()%10000));
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(soldier + ":完成任务");
        }

        public static class BarrierRun implements Runnable {

            private int N;
            private boolean flag;
            public BarrierRun(int N, boolean flag) {
                this.N = N;
                this.flag = flag;
            }
            @Override
            public void run() {
                if (flag) {
                    System.out.println(N + "个士兵任务完成");
                }
                else {
                    System.out.println(N + "个士兵集合完毕");
                }
            }
        }

        public static void main(String[] args) {
            int N = 10;
            Thread[]  soldiers = new Thread[N];
            CyclicBarrier cyclicBarrier = new CyclicBarrier(N, new BarrierRun(N, false));
            System.out.println("队伍集合完毕");
            for (int i = 0; i < N; i++) {
                System.out.println("soldier" + i + "报道");
                soldiers[i] = new Thread(new Soldier(cyclicBarrier, "士兵" + i));
                soldiers[i].start();
            }
        }

    }

}
