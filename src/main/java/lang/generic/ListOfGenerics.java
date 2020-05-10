package lang.generic;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

class GenericArray<T> {
    /**
     * 将数组类型声明为泛型类型
     */
    private T[] array;

    @SuppressWarnings("unchecked")
    public GenericArray(int sz) {
        array = (T[]) new Object[sz];
    }

    public T get(int index) {
        return array[index];
    }

    public void put(int index, T item) {
        array[index] = item;
    }

    public T[] rep() {
        return array;
    }

}

class GenericArray2<T> {
    /**
     *  泛型数组直接声明为object类型 取元素时向上转型 但是实际运行时底层依然是object无法向上转型
      */
    private Object[] array;

    public GenericArray2(int sz) {
        array = new Object[sz];
    }

    public void put(int index, T item) {
        array[index] = item;
    }

    public T get(int index) {
        return (T) array[index];
    }

    public T[] rep() {
        return (T[]) array;
    }
}

class GenericArrayWithTypeToken<T> {
    /**
     * 创建泛型数组的正确打开方式
     */
    private T[] array;
    public GenericArrayWithTypeToken(Class<T> type, int sz) {
        array = (T[]) Array.newInstance(type, sz);
    }

    public void put(int index, T item) {
        array[index] = item;
    }

    public T get(int index) {
        return array[index];
    }

    public T[] rep() {
        return array;
    }
}
/**
 * 〈泛型列表〉<br>
 * 不能创建泛型数组 通过list来实现
 * 数组将跟踪它们的实际类型 这个类型是在数组被创建时确定的 编译器已经确定 不可以再转型
 * 成功创建泛型数组的唯一方式就是创建一个被擦除类型的新数组 然后对其进行转型
 *
 * @author Carrie
 * @create 2020/5/10
 * @since 1.0.0
 */
public class ListOfGenerics<T> {
    private List<T> array = new ArrayList<T>();

    public void add(T item) {
        this.array.add(item);
    }

    public T get(int index) {
        return array.get(index);
    }

    public static void main(String[] args) {
        GenericArray<Integer> ga = new GenericArray<>(10);
        ga.get(0);
        ga.put(2, 10);
        System.out.println(ga.get(2));
        System.out.println(ga.get(2).getClass().getSimpleName());
        // 运行时数组是object 不能转型为integer
        //Integer[] integers = ga.rep();
        Object[] objects = ga.rep();

        GenericArray2<Integer> ga2 = new GenericArray2<>(10);
        ga2.put(3, 10);
        System.out.println(ga2.get(3));
        System.out.println(ga2.get(3).getClass().getSimpleName());
        // 运行时数组是object 不能转型为integer
//        Integer[] integers1 = ga2.rep();
        Object[] objects1 = ga2.rep();
        GenericArrayWithTypeToken<Integer> ga3 = new GenericArrayWithTypeToken<>(Integer.class, 10);
        ga3.put(0, 12);
        System.out.println(ga3.get(0));
        Integer[] integers3 = ga3.rep();
        System.out.println(integers3);

    }
 }