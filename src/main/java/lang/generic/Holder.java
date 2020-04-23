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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Holder<?> holder = (Holder<?>) o;
        return Objects.equals(value, holder.value);
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
//        fruit.setValue(apple);
//        fruit.setValue(new Fruit());
        Fruit f = fruit.getValue();
        Apple a = (Apple) fruit.getValue();
        System.out.println(fruit.equals(a));
    }
}