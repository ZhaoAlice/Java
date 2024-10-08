package lang.java8.streams;

import java.util.Random;
import java.util.stream.IntStream;

/**
 * 〈the first stream programing〉<br>
 *
 * @author Carrie
 * @create 2020/4/13
 * @since 1.0.0
 */
public class Randoms {
    public static void main(String[] args) {
        new Random(47)
                .ints(5, 20)
                .distinct()
                .limit(7)
                .sorted()
                .forEach(System.out::println);
        System.out.println(IntStream.range(10, 20).sum());
    }

}