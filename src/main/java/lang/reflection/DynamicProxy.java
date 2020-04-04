package lang.reflection;

/**
 * 〈动态代理〉<br>
 *
 * @author Carrie
 * @create 2020/4/4
 * @since 1.0.0
 */
public class DynamicProxy implements Interface {

    public void doSomething() {
        System.out.println("do something");
    }

    public void doSomethingElse(String str) {
        System.out.println("do something else" + str);
    }
}