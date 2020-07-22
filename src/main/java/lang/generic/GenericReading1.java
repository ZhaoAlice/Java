package lang.generic;

import java.util.Arrays;
import java.util.List;

/**
 * 〈超类型通配符〉<br>
 *
 * @author Carrie
 * @create 2020/4/23
 * @since 1.0.0
 */
class SuperTypeWildcards1 {
    static void writeTo(List<? super Apple> apples) {
        apples.add(new Apple());
        apples.add(new Jonathan());
        // Apple是下界 可以添加其及其子类的对象 任何基类都不能添加
//        apples.add(new Fruit());
    }
}

public class GenericReading1 {

    static List<Apple> apples = Arrays.asList(new Apple());
    static List<Fruit> fruits = Arrays.asList(new Fruit());

    static <T> T readExact(List<T> list) {
        return list.get(0);
    }

    static void f1() {
        Fruit f1 = readExact(apples);
        Fruit f2 = readExact(fruits);
        Apple apple = readExact(apples);
    }

    static class Reader<T> {
        T readExact(List<T> list) {
            return list.get(0);
        }
    }
    static void f2() {
        Reader<Fruit> fruitReader = new Reader<>();
        Reader<Apple> appleReader = new Reader<>();

        // 不兼容
//        Fruit f1 = fruitReader.readExact(apples);
        Fruit f2 = fruitReader.readExact(fruits);
        Apple apple = appleReader.readExact(apples);
    }

    static class CovariantReader<T> {
        T readExact(List<? extends T> list) {
            return list.get(0);
        }
    }
    static void f3() {
        CovariantReader<Fruit> fruitReader = new CovariantReader<>();
        CovariantReader<Apple> appleReader = new CovariantReader<>();

        // 可以了
        Fruit f1 = fruitReader.readExact(apples);
        Fruit f2 = fruitReader.readExact(fruits);
        Apple apple = appleReader.readExact(apples);
    }

    public static void main(String[] args) {
        f1();
        f2();
        f3();
    }
}