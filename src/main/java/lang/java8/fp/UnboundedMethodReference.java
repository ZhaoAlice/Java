package lang.java8.fp;

/**
 * 〈没有方法引用的对象〉<br>
 *
 * @author Carrie
 * @create 2020/4/10
 * @since 1.0.0
 */
class X {
    String f() {
        return "X::f()";
    }
}

interface MakeString {
    String make();
}

interface TransformX {
    String transformx(X x);
}
public class UnboundedMethodReference {

    public static void main(String[] args) {
        // 没有对象的方法引用需要将隐藏的参数传递到接口方法的第一个参数中
        //MakeString ms = X::f;
        TransformX sp = X::f;
        X x = new X();
        // x即隐藏参数this 然后可以调用f
        System.out.println(sp.transformx(x));
        System.out.println(x.f());
    }
}