/**
 * Copyright (C), 2019
 * FileName: NestedInterface
 * Author:   ZLG
 * Date:     2019/6/30 20:12
 * Description: 嵌套类测试
 * History:
 * <author>          <time>          <version>          <desc>
 */
package lang.interfaces;

/**
 * 〈一句话功能简述〉<br> 
 * 〈嵌套类测试〉
 *
 * @author ZLG
 * @create 2019/6/30
 * @since 1.0.0
 */
class A {
    interface B {
        void f();
    }
    public class Bimp implements B {
        @Override
        public void f() {

        }
    }
    private interface C {
        void f();
    }
    public C getC() {
        return new Cimp();
    }
    public void receive(C c) {
        cDef = c;
        cDef.f();
    }
    public int g() {
        return 0;
    }
    private C cDef;
    public class Cimp implements C {
        @Override
        public void f() {

        }
    }
}
public class NestedInterface {

    public static void main(String[] args) {
        A a = new A();
        a.new Cimp().f();
        // C is private field, cannot be access here
//        A.C c = a.getC();
        A.Cimp cimp = (A.Cimp) a.getC();
        a.receive(a.getC());
    }
}