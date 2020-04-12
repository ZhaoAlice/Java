package lang.java8.fp;

/**
 * 〈自定义多参数函数式接口〉<br>
 *
 * @author Carrie
 * @create 2020/4/12
 * @since 1.0.0
 */
@FunctionalInterface
public interface TriFunctional<R, U, V, W> {
    R apply(U u, V v, W w);
}