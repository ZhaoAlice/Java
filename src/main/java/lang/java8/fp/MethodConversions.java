package lang.java8.fp;

import java.util.function.BiConsumer;

/**
 * 〈〉<br>
 *
 * @author Carrie
 * @create 2020/4/11
 * @since 1.0.0
 */
class In1 {

}
class In2 {

}
public class MethodConversions {
    static void accept(In1 in1, In2 in2) {
        System.out.println("accept()");
    }

    static void someOtherName(In1 in1, In2 in2) {
        System.out.println("some other name");
    }

    public static void main(String[] args) {
        BiConsumer<In1, In2> bi;
        // 方法引用 只要参数类型与返回类型与函数式接口中的方法相同就行
        // java会将方法映射到接口方法
        bi = MethodConversions::accept;
        bi.accept(new In1(), new In2());
        bi = MethodConversions::someOtherName;
        bi.accept(new In1(), new In2());
    }

}