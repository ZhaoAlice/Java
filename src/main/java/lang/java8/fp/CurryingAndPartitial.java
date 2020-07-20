package lang.java8.fp;

import java.util.function.Function;

/**
 * 〈柯里化与部分求值〉<br>
 *  柯里化即将一个含有多参数的函数转换为单参数的函数
 *
 * @author Carrie
 * @create 2020/4/12
 * @since 1.0.0
 */
public class CurryingAndPartitial {
    static String uncurried(String a, String b) {
        return a + b;
    }

    public static void main(String[] args) {
        // 其中一个类型参数为函数接口 级联的箭头函数
        Function<String, Function<String, String>> sum =
                a -> b -> a + b;
        System.out.println(uncurried("hi","hello"));
        Function<String, String> f = sum.apply("hi");
        f.apply("hello");
    }
}