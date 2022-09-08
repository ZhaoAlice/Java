package lang.java8.intf;

/**
 * 〈〉<br>
 *
 * @author Carrie
 * @create 2022/9/8
 * @since 1.0.0
 */
public interface IntfT2 {
    default void test() {
        testStaticMethod();
    }

    static void testStaticMethod() {

    }
}