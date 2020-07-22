package lang.java8.streams.optional;

import java.util.stream.Stream;

/**
 * 〈test optional first time simply〉<br>
 *
 * @author Carrie
 * @create 2020/7/10
 * @since 1.0.0
 */
public class OptionalsFromEmptyStreams {

    public static void main(String[] args) {
        System.out.println(Stream.<String>empty().findFirst());
        System.out.println(Stream.<String>empty().findAny());
        System.out.println(Stream.<String>empty().max(String.CASE_INSENSITIVE_ORDER));
        System.out.println(Stream.<String>empty().min(String.CASE_INSENSITIVE_ORDER));


    }
}