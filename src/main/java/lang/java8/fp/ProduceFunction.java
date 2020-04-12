package lang.java8.fp;

import java.util.function.Function;

/**
 * 〈高阶函数-产生函数的函数〉<br>
 *
 * @author Carrie
 * @create 2020/4/12
 * @since 1.0.0
 */
interface FunctionSS extends Function<String, String>{}
public class ProduceFunction {
    static FunctionSS produce() {
        return s -> s.toUpperCase();
    }

    public static void main(String[] args) {
        FunctionSS functionSS = produce();
        functionSS.apply("test higher order function");
    }
}