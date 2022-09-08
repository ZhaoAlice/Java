package lang.java9;

/**
 * 〈jdk9接口中支持私有方法 有什么用?〉<br>
 *  对jdk8提出的default和Public static method的补充
 *  接口的私有方法和私有静态方法不可被实现该接口的类或继承该接口的接口调用或重写
 *  单继承 多实现
 * @author Carrie
 * @create 2022/9/8
 * @since 1.0.0
 */
public interface IPrivateGrammar {


    /**
     * default 只能修饰实例方法
     * 可以调用私有静态方法和私有实例方法
     */
    default void testPublicStaticMethod() {
        testPrivateStaticMethod();
        testPrivateInstanceMethod();
    }

    /**
     * 接口中的静态私有方法 只能在该接口中被使用
     */
    private static void testPrivateStaticMethod() {

    }

    /**
     * 私有实例方法
     */
    private void testPrivateInstanceMethod() {

    }
}