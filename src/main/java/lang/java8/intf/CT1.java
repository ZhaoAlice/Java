package lang.java8.intf;

/**
 * 〈〉<br>
 *
 * @author Carrie
 * @create 2022/9/8
 * @since 1.0.0
 */
public class CT1 implements IntfT1, IntfT2 {
    /**
     * 如果类实现的接口中 有相同的default的方法 则必须在类中实现该方法
     * 否则编译器不知道应该调用哪个接口中的默认方法
     * 或者一个接口继承另一个接口 将方法声明为抽象方法
     */
    @Override
    public void test() {
        IntfT1.super.test();
    }
}