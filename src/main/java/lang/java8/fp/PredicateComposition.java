package lang.java8.fp;

import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * 〈短路组合函数〉<br>
 *
 * @author Carrie
 * @create 2020/4/12
 * @since 1.0.0
 */
public class PredicateComposition {
    static Predicate<String> p1 = s -> s.contains("bar"),
            p2 = s -> s.length() < 5,
            p3 = s -> s.contains("foo"),
    //取反 and or 谓词组合
            p4 = p1.negate().and(p2).or(p3);

    public static void main(String[] args) {
        Stream.of("fooa", "bar1", "foobar", "foobaz","foongofukey")
                .filter(p4).forEach(System.out::println);
    }
}