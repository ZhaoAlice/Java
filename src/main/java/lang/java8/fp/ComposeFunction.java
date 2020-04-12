package lang.java8.fp;

import java.util.function.Function;

/**
 * 〈组合函数〉<br>
 *
 * @author Carrie
 * @create 2020/4/12
 * @since 1.0.0
 */
public class ComposeFunction {
    static Function<String, String> f1 = s -> {
        System.out.println(s);
        return s.replace('A', '_');
    },
            f2 = s -> s.substring(3),
            f3 = s -> s.toUpperCase(),
            f4 = f1.compose(f2).andThen(f3);

    public static void main(String[] args) {
        System.out.println(f4.apply("Go After All Ambulances"));
    }
}