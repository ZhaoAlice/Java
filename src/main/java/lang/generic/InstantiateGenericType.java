package lang.generic;

/**
 * 〈创建泛型参数对应类型实例的常用方法〉<br>
 *  使用显式的工厂
 *
 * @author Carrie
 * @create 2020/5/9
 * @since 1.0.0
 */
class ClassAsFactory<T> {
    T x;

    public ClassAsFactory(Class<T> type) {
        try {
            x = type.newInstance();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

interface FactoryI<T> {
    T create();
}

class Foo2<T> {
    private T t;
    public <F extends FactoryI<T>> Foo2(F factory) {
        t = factory.create();
    }
}

class IntegerFactory implements FactoryI<Integer> {

    @Override
    public Integer create() {
        return new Integer(0);
    }
}

class Widget {
    public static class Factory implements FactoryI<Building> {

        @Override
        public Building create() {
            return new Building();
        }
    }
}
/**
 * @author ZLG
 */
public class InstantiateGenericType {
    public static void main(String[] args) {
        new Foo2<Integer>(new IntegerFactory());
        new Foo2<Building>(new Widget.Factory());
    }
}