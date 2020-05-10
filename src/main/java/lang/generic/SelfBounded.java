package lang.generic;


/**
 * 古怪的循环泛型
 * 我在创建一个类 它继承自一个泛型类型 这个泛型类型接受我的类的名字作为其参数
 * 当给出导出类的名字时 这个泛型基类能够实现什么呢 java中泛型关乎参数与返回类型 因此它能够产生使用导出类作为其参数和返回类型
 * 的基类 它还能够将导出类型用作其域类型 甚至那些将被擦除为object的类型
 * @param <T>
 */
class GenericType<T> {

}

class CuriouslyRecurringGeneric extends GenericType<CuriouslyRecurringGeneric> {

}

class BasicHolder<T> {
    T element;

    void setElement(T element) {
        this.element = element;
    }

    T getElement() {
        return element;
    }

    void f() {
        System.out.println(element.getClass().getSimpleName());
    }
}

/**
 * 这里非常重要的知识点 子类接受的参数和返回的值具有子类类型 而不仅仅是基类的类型
 * CRG的本质就是基类用导出类替代其参数
 * 这意味着泛型基类变成了一种其所有导出类的公共功能的模板 但是这些功能对于其所有参数和返回值 将使用导出类型
 * 在所产生的类中将使用确切类型而不是基类型
 */
class Subtype extends BasicHolder<Subtype> {

}

/**
 * 模板基类可以使用任何类型作为其泛型参数
 */
class Other{}

class BasicOther extends BasicHolder<Other> {
}
/**
 * 自限定将强制泛型当作其自己的边界参数来使用
 */
class SelfBounding<T extends SelfBounding<T>> {
    T element;

    SelfBounding<T> set(T item) {
        element = item;
        return this;
    }

    T get() {
        return element;
    }
}

/**
 * 自限定所做的就是要求在继承关系中 向下面那样使用这个类 强制要求将正在定义的类当做参数传给基类
 * 这种是常见的用法
 * 自限定限制只能强制作用于继承关系 如果使用自限定 就应该了解这个类所用的类型参数将与使用这个参数的类具有相同的基类型
 * 强制要求使用这个类的每个人都要遵循这种形式
 */
class A extends SelfBounding<A> {}
class B extends SelfBounding<A> {}
class C extends SelfBounding<C> {
    C setAndGet(C arg) {
        set(arg);
        return get();
    }
}
class D {}
/*class E extends SelfBounding<D> {
}*/
class F extends SelfBounding {}

/**
 * 自限定用于泛型方法
 */
class SelfBoundedMethod {
    static <T extends SelfBounding<T>> T f(T arg) {
        return arg.set(arg).get();
    }
}
/**
 * 〈自限定类型〉<br>
 *
 * @author Carrie
 * @create 2020/5/10
 * @since 1.0.0
 */
public class SelfBounded<T extends SelfBounded<T>> {
    public static void main(String[] args) {
        Subtype subtype = new Subtype();
        Subtype subtype1 = new Subtype();

        subtype.setElement(subtype1);
        Subtype subtype3 = subtype.getElement();

        subtype.f();
        BasicOther basicOther = new BasicOther();
        basicOther.setElement(new Other());
        Other other = basicOther.getElement();

        A a = new A();
        a.set(new A());

        a = a.set(new A()).get();
        a = a.get();
        C c = new C();

        c = c.setAndGet(new C());
        A a1 = SelfBoundedMethod.f(new A());
    }
}