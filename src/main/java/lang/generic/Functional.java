package lang.generic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

interface Combiner<T> {
    T combine(T x, T y);
}

interface UnaryFunction<R, T> {
    R function(T t);
}

interface Collector<T> extends UnaryFunction<T, T> {
    T result();
}

interface UnaryPredicate<T> {
    boolean test(T t);
}

/**
 * 〈函数对象 就是java中的函数式编程〉<br>
 *
 * @author Carrie
 * @create 2020/5/17
 * @since 1.0.0
 */
public class Functional {
    public static <T> T reduce(Iterable<T> seq, Combiner<T> combiner) {
        Iterator<T> iterator = seq.iterator();
        if (iterator.hasNext()) {
            T result = iterator.next();
            while (iterator.hasNext()) {
                result = combiner.combine(result, iterator.next());
            }
            return result;
        }
        return null;
    }

    public static <T> Collector<T> forEach(Iterable<T> seq, Collector<T> func) {
        for (T t: seq ) {
            func.function(t);
        }
        return func;
    }

    public static <T> List<T> filter(Iterable<T> seq, UnaryPredicate<T> predicate) {
        List<T> list = new ArrayList<>();
        for (T t: seq) {
            if (predicate.test(t)) {
                list.add(t);
            }
        }
        return list;
    }

    /**
     * 输入T 返回R
     * @param seq
     * @param func
     * @param <R>
     * @param <T>
     * @return
     */
    public static <R, T> List<R> transform(Iterable<T> seq, UnaryFunction<R, T> func) {
        List<R> list = new ArrayList<>();
        for (T r : seq) {
            list.add(func.function(r));
        }
        return list;
    }
}