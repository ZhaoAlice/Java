package net;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * 〈〉<br>
 *
 * @author Carrie
 * @create 2023/3/2
 * @since 1.0.0
 */
public class test {


    //public static void main(String[] args) throws UnsupportedEncodingException {
    //    //BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    //    //System.out.println(encoder.encode("11"));
    //    // 创建一个单线程执行器
    //    ExecutorService executor = Executors.newSingleThreadExecutor();
    //    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    //    PrintStream customOut = new PrintStream(baos, false, StandardCharsets.UTF_8.name());
    //    PrintStream defaultp = System.out;
    //    try {
    //        // 创建一个任务来执行命令
    //        Callable<Void> task = () -> {
    //            // 只在这个线程中重定向输出
    //            System.setOut(customOut);
    //            System.out.println("aaaaaaaaaaa");
    //            return null;
    //        };
    //
    //        // 提交任务并等待结果
    //        Future<Void> future = executor.submit(task);
    //        future.get(10, TimeUnit.SECONDS);
    //        System.setOut(defaultp);
    //        System.out.println(baos.toString(StandardCharsets.UTF_8));
    //
    //    }
    //    catch (Exception e) {
    //
    //    }
    //    finally {
    //        customOut.close();
    //        executor.shutdownNow();
    //    }
    //
    //}
    public static void main(String[] args) {
        Flux.range(1, 5)
            .map(i -> {
                System.out.println("Mapping on thread: " + Thread.currentThread().getName());
                return i;
            })
            .publishOn(Schedulers.parallel()) // 切换到并行调度器
            .map(i -> {
                System.out.println("Processing on thread: " + Thread.currentThread().getName());
                return i * 10;
            })
            .subscribe(result -> System.out.println("Result: " + result + " on thread: " + Thread.currentThread().getName()));
    }
}