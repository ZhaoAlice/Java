package lang.generic;

abstract class GenericCreate<T> {
    final T element;
    GenericCreate() {
        this.element = create();
    }

    abstract T create();
}
class X {}

class Creator extends GenericCreate<X> {
    @Override
    X create() {
        return new X();
    }

    void f() {
        System.out.println(element.getClass().getSimpleName());
    }
}
/**
 * 〈模板方式设计模式创建泛型参数实例〉<br>
 *
 * @author Carrie
 * @create 2020/5/10
 * @since 1.0.0
 */
public class GenericWithCreate {
    public static void main(String[] args) {
        Creator c = new Creator();
        c.f();
    }
}