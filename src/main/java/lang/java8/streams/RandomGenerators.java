package lang.java8.streams;

import java.util.Random;
import java.util.stream.Stream;

/**
 * 〈随机数流〉<br>
 *
 * @author Carrie
 * @create 2020/4/16
 * @since 1.0.0
 */
public class RandomGenerators {
    public static <T> void show(Stream <T> stream) {
        stream
                .limit(4)
                .forEach(System.out::println);
        System.out.println("############");
    }

    public static void main(String[] args) {
        Random random = new Random(47);
        // 生成普通的流
        show(random.ints().boxed());
        show(random.longs().boxed());
        show(random.doubles().boxed());
        // 控制流的上限与下限
        show(random.ints(2,7).boxed());
        show(random.longs(50, 100).boxed());
        show(random.doubles(20, 30.5).boxed());

        // 控制流大小
        show(random.ints(7).boxed());
        show(random.longs(100).boxed());
        show(random.doubles(30).boxed());

        // 控制流大小和界限
        show(random.ints(2, 7, 10).boxed());
        show(random.longs(3, 30, 100).boxed());
        show(random.doubles(5, 0.2, 30).boxed());

    }

}