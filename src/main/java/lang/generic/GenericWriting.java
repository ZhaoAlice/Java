package lang.generic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 协变通配符 ? extends T 适用于读取 至少返回的是T或者其子类型 但是不能向里面塞入对象
 * 因为编译器无法知道你究竟要装入何种类型?的对象 因此拒绝写入
 *
 * 逆变通配符 ? extends T 适用于写入 T为下界 编译器知道你可以塞入T及其子类型对象
 * 但是不允许读取 因为它不知道?代表什么类型 只能是object
 */
class GenericReading {
    static <T> T readExact(List<T> list) {
        return list.get(0);
    }

    static List<Fruit> fruits = Arrays.asList(new Fruit());
    static List<Apple> applelist = Arrays.asList(new Apple());

    static void f1() {
        Apple a = readExact(applelist);
        Fruit f = readExact(applelist);
        Fruit f1 = readExact(fruits);
    }

    static class Reader<T> {
        T readExact(List<T> list) {
            return list.get(0);
        }
    }
    static void f2() {
        Reader<Apple> appleReader = new Reader<>();
        Apple a = appleReader.readExact(applelist);
        Fruit f = appleReader.readExact(applelist);
        // T 限定为只能是apple类型的对象
//        Fruit f1 = appleReader.readExact(fruits);
        Reader<Fruit> fruitReader = new Reader<>();
        Fruit f1 = fruitReader.readExact(fruits);
//        f1 = fruitReader.readExact(applelist);
    }

    static class CovariantReader<T> {
        /**
         * 协变参数
         * @param list
         * @return
         */
        T readExact(List<? extends T> list) {
            return list.get(0);
        }
    }

    static void f3() {
        CovariantReader<Apple> appleCovariantReader = new CovariantReader<>();
        Apple a = appleCovariantReader.readExact(applelist);
//        Fruit f = appleCovariantReader.readExact(fruits);
        CovariantReader<Fruit> fCovariantReader = new CovariantReader<>();
        Fruit f = fCovariantReader.readExact(applelist);
        System.out.println(f);
        f = fCovariantReader.readExact(fruits);
        System.out.println(f);
        a = (Apple) fCovariantReader.readExact(applelist);
        System.out.println(a);
    }
}
/**
 * 〈泛型类型写入和读取思考〉<br>
 *  因此你可能会根据如何向一个泛型类型“写入”(传入给写个方法),以及如何能够从一个泛型类型中"读取"(从一个方法中返回)
 *  来思考子类型和超类型边界
 *
 * @author Carrie
 * @create 2020/5/10
 * @since 1.0.0
 */
public class GenericWriting {
    static <T> void writeExact(List<T> list, T item) {
        list.add(item);
    }

    static List<Apple> apples = new ArrayList<Apple>();
    static List<Fruit> fruits = new ArrayList<Fruit>();

    static void f() {
        // 没有问题的 这的确是fruit的子类型
        fruits.add(new Apple());
        writeExact(apples, new Apple());
//        writeExact(apples, new Fruit());
        // 可以添加子类的对象 跟书中所说不一样 我想他这里想要表达式子类型通配符
        writeExact(fruits, new Apple());
        writeExact(fruits, new Fruit());
        System.out.println(fruits);
    }

    /**
     * 逆变参数
     * 这个list将持有T类型 或者 从T导出的某种具体类型
     * @param list
     * @param item
     * @param <T>
     */
    static <T> void writeWildcard(List<? super T> list, T item) {
        list.add(item);
    }

    /**
     *
     */
    static void f1() {
        writeWildcard(apples, new Apple());
//        writeWildcard(apples, new Fruit());
        writeWildcard(fruits, new Apple());
        writeWildcard(fruits, new Fruit());
        System.out.println(fruits);
    }

    public static void main(String[] args) {
        f(); f1();
        GenericReading.f1();
        GenericReading.f2();
        GenericReading.f3();

    }
}