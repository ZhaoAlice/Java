package lang.java8.fp;

import java.util.function.DoubleToIntFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntToDoubleFunction;
import java.util.function.ToLongFunction;

/**
 * 〈函数变体〉<br>
 *
 * @author Carrie
 * @create 2020/4/11
 * @since 1.0.0
 */
class Fool {
}

class Bar {
    Fool f;

    Bar(Fool f) {
        this.f = f;
    }
}

class Ibaz {
    int i;

    Ibaz(int i) {
        this.i = i;
    }
}

class Lbaz {
    long l;

    public Lbaz(long l) {
        this.l = l;
    }
}

class Dbaz {
    double d;

    public Dbaz(double d) {
        this.d = d;
    }
}

public class FunctionVariants {
    // 接受一个Fool 返回一个Bar
    static Function<Fool, Bar> f1 = f -> new Bar(f);
    // 接受一个int 返回一个Ibaz long double 类似
    static IntFunction<Ibaz> f2 = i -> new Ibaz(i);
    // 接受一个Lbaz 返回一个long int double类似
    static ToLongFunction<Lbaz> fl = lb -> lb.l;

    // 基本累心之间的转换
    static IntToDoubleFunction i2d = i -> i;
    // 类型转换
    static DoubleToIntFunction d2i = d -> (int) d;

    public static void main(String[] args) {
        // 将fool作为输入 返回bar实例
        Bar b = f1.apply(new Fool());

        int i = d2i.applyAsInt(1.0);

        long l = fl.applyAsLong(new Lbaz(10l));
    }
}