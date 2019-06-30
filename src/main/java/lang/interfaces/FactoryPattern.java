/**
 * Copyright (C), 2019
 * FileName: FactoryPattern
 * Author:   ZLG
 * Date:     2019/6/30 20:01
 * Description: 工厂设计模式简单示例
 * History:
 * <author>          <time>          <version>          <desc>
 */
package lang.interfaces;

/**
 * 〈一句话功能简述〉<br> 
 * 〈工厂设计模式简单示例〉
 *
 * @author ZLG
 * @create 2019/6/30
 * @since 1.0.0
 */
interface Service {
    void method1();
    void method2();
}
interface ServiceFactory {
    Service getService();
}
class Imp1 implements Service {

    Imp1() {

    }
    @Override
    public void method1() {
        System.out.println("Imp1 method1");
    }

    @Override
    public void method2() {
        System.out.println("Imp1 method2");
    }
}
class Imp1Factory implements ServiceFactory {

    @Override
    public Service getService() {
        return new Imp1();
    }
}

class Imp2 implements Service {

    Imp2() {

    }
    @Override
    public void method1() {
        System.out.println("Imp2 method1");
    }

    @Override
    public void method2() {
        System.out.println("Imp2 method2");
    }
}
class Imp2Factory implements ServiceFactory {

    @Override
    public Service getService() {
        return new Imp2();
    }
}
public class FactoryPattern {
    public static void serviceFactory(ServiceFactory serviceFactory) {
        Service service = serviceFactory.getService();
        service.method1();
        service.method2();
    }

    public static void main(String[] args) {
        FactoryPattern.serviceFactory(new Imp1Factory());
        FactoryPattern.serviceFactory(new Imp2Factory());
    }
}