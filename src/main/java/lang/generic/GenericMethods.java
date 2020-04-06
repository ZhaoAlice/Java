package lang.generic;

/**
 * 〈泛型方法 只需将泛型参数列表置于返回值之前〉<br>
 *
 * @author Carrie
 * @create 2020/4/6
 * @since 1.0.0
 */
public class GenericMethods {
    // 方法参数中有类型参数时 调用时会进行类型参数推断
    // 类型参数推断只对赋值语句有效
    public <T> void f(T t) {
        System.out.println(t.getClass().getName());
    }

    public static void main(String[] args) {
        GenericMethods genericMethods = new GenericMethods();
        genericMethods.f("aa");
        genericMethods.f(3);
        genericMethods.f('C');
        genericMethods.f(1.0f);
    }
}