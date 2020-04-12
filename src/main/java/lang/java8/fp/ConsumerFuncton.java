package lang.java8.fp;

import java.util.function.Function;

/**
 * 〈消费一个函数〉<br>
 *
 * @author Carrie
 * @create 2020/4/12
 * @since 1.0.0
 */
class One {}
class Two {}
public class ConsumerFuncton {
    static Two consume(Function<One, Two> fun) {
        return fun.apply(new One());
    }
    static String consume1(Function<String, String> fun) {
        return fun.apply("test produce and consume");
    }
    public static void main(String[] args) {
        // 传递的参数是一个函数引用
        Two two = consume(one -> new Two());
        String s = consume1(ProduceFunction.produce());
    }
}