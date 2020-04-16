package lang.java8.fp;

import java.util.function.Function;

/**
 * 〈基于消费函数生成新函数〉<br>
 *
 * @author Carrie
 * @create 2020/4/12
 * @since 1.0.0
 */
class I implements IntCall{
    @Override
    public String toString() {
        return "I{}";
    }

    @Override
    public int call(int arg) {
        return 0;
    }
}
class O {

    @Override
    public String toString() {
        return "O{}";
    }
}
public class TransformFunction {
    public I getI() {
        return new I();
    }
    static Function<I, O> transform(Function<I, O> in) {
        return in.andThen(o -> {
            System.out.println(o);
            return o;
        });
    }
    static Function<I, O> compose(Function<I, O> in) {
        return in.compose(o -> {
            System.out.println(o);
            return o;
        });
    }
    public static void main(String[] args) {
        // 将函数引用作为参数传递 然后对该函数包一层 andThen先执行in 再执行外边的函数
        // 返回的是包装过的新函数
        Function<I, O> f2 = transform(i -> {
            System.out.println(i);
            return new O();
        });
        O o = f2.apply(new I());

        // 先执行compose中的apply函数 再执行this apply
        Function<I, O> f3 = compose(i -> {
            System.out.println(i);
            return new O();
        });
        O o1 = f3.apply(new I());
    }
}