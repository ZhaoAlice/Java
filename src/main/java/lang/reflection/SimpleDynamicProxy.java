package lang.reflection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 〈动态代理处理器〉<br>
 *
 * @author Carrie
 * @create 2020/4/4
 * @since 1.0.0
 */
class DynamicProxyHandler implements InvocationHandler {
    private DynamicProxy dynamicProxy;

    public DynamicProxyHandler(DynamicProxy dynamicProxy) {
        this.dynamicProxy = dynamicProxy;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("dynamic proxy class: " + proxy.getClass() + "method: " + method + "args: " + args);
        if (args != null) {
            for (Object arg : args) {
                System.out.println("  " + arg);
            }
        }
        return method.invoke(dynamicProxy, args);
    }
}

class SimpleDynamicProxy {
    public static void consumer(Interface inf) {
        inf.doSomething();
        inf.doSomethingElse("else");
    }

    public static void main(String[] args) {
        DynamicProxy dynamicProxy = new DynamicProxy();
        consumer(dynamicProxy);

        // 利用反射
        Interface proxy = (Interface) Proxy.newProxyInstance(
                Interface.class.getClassLoader(),
                new Class[]{Interface.class},
                new DynamicProxyHandler(dynamicProxy));
        consumer(proxy);
    }
}