package lang.java8.fp;

import java.util.Comparator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * 〈类的函数式引用〉<br>
 *
 * @author Carrie
 * @create 2020/4/11
 * @since 1.0.0
 */
class AA{}
class BB{}
class CC{}

public class ClassFunctional {
    static AA f1() {
        return new AA();
    }

    static int f2(AA aa1, AA aa2) {
        return 1;
    }

    static void f3(AA aa) {}
    static void f4(AA aa, BB bb){}

    static CC f5(AA aa) {
        return new CC();
    }

    static CC f6(AA aa, BB bb) {
        return new CC();
    }

    static boolean f7(AA aa) {
        return true;
    }

    static boolean f8(AA aa, BB bb) {
        return true;
    }

    static AA f9(AA aa) {
        return new AA();
    }

    static AA f10(AA aa1, AA aa2) {
        return new AA();
    }
    static int f(int i, long l, double d ) {
        return 0;
    }
    public static void main(String[] args) {

        Supplier<AA> s = ClassFunctional::f1;
        s.get();
        Comparator<AA> c = ClassFunctional::f2;
        c.compare(new AA(), new AA());
        Consumer<AA> cons = ClassFunctional::f3;
        cons.accept(new AA());
        BiConsumer<AA, BB> bi = ClassFunctional::f4;
        bi.accept(new AA(), new BB());
        Function<AA, CC> function = ClassFunctional::f5;
        function.apply(new AA());
        BiFunction<AA, BB, CC> bif1 = ClassFunctional::f6;
        bif1.apply(new AA(), new BB());
        Predicate<AA> pre = ClassFunctional::f7;
        boolean result = pre.test(new AA());
        BiPredicate<AA, BB> bipre = ClassFunctional::f8;
        result = bipre.test(new AA(), new BB());
        // 适用于操作数与返回结果同一类型的场景 unary operand 一元操作数
        UnaryOperator<AA> uo = ClassFunctional::f9;
        uo.apply(new AA());
        UnaryOperator<AA> uo1 = UnaryOperator.identity();
        // 两个或以上同等类型的操作数 并且返回同一类型的操作数
        BinaryOperator<AA> bio = ClassFunctional::f10;
        bio.apply(new AA(), new AA());
        TriFunctional<Integer, Integer, Long, Double> tri = ClassFunctional::f;
        tri.apply(1, 2L, 3.0);
    }
}