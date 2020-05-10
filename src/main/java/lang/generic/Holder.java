package lang.generic;

import java.util.Objects;

/**
 * 〈通配符2〉<br>
 *
 * @author Carrie
 * @create 2020/4/23
 * @since 1.0.0
 */
public class Holder<T> {
    private T value;

    public Holder() {

    }
    public Holder(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
//        Holder<?> holder = (Holder<?>) o;
        return value.equals(o);
//        return Objects.equals(value, holder.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public static void main(String[] args) {
        Apple apple = new Apple();
        Apple apple1 = new Apple();

        Holder<Apple> holder = new Holder<>(apple);
        holder.setValue(apple1);
        apple1 = holder.getValue();
//        Holder<Fruit> fruit1 = apple;
        Holder<? extends Fruit> fruit = holder;
        // 编译器将认为你可以塞入? extends Fruit表示的任何类型的对象 这是不允许的
//        fruit.setValue(apple);
//        fruit.setValue(new Fruit());
        Fruit f = fruit.getValue();
        System.out.println(fruit.getValue().getClass().getSimpleName());
        // 你可以转型为apple 返回的是apple
        Apple a = (Apple) fruit.getValue();
        // equals工作良好 因为它将接受object而不是T类型的参数 编译器只关注传递进来和要返回的对象类型 并不会分析代码
        System.out.println(fruit.equals(a));
        System.out.println(fruit.equals(a));
    }
}