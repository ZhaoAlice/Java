package lang.java8.streams;

import java.util.function.IntConsumer;

import static java.util.stream.IntStream.*;
/**
 * 〈循环〉<br>
 *
 * @author Carrie
 * @create 2020/4/20
 * @since 1.0.0
 */
public class Repeat {
    public static void repeat(int n, IntConsumer action) {
        range(0, n).forEach(i -> action.accept(i));
    }

    static void hi(int i) {
        System.out.println(i);
    }

    public static void main(String[] args) {
     repeat(3, i -> System.out.println(i));
     repeat(2, Repeat::hi);
    }
}