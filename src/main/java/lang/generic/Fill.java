package lang.generic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 〈泛化不足〉<br>
 *
 * @author Carrie
 * @create 2020/5/17
 * @since 1.0.0
 */
public class Fill {
    public static <T> void fill(Collection<T> collection, Class<? extends T> classToken, int size) {
        try {
            collection.add(classToken.newInstance());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}

class Contract {
    private static long  counter = 0;
    private final long id = counter++;

    @Override
    public String toString() {
        return getClass().getName() + " " + id;
    }
}

class TitleTransfer extends Contract {

}

class FillTest {
    public static void main(String[] args) {
        List<Contract> list = new ArrayList<>();
        Fill.fill(list, Contract.class, 5);
        Fill.fill(list, TitleTransfer.class, 5);
        for (Contract c : list) {
            System.out.println(c);
        }
        SimpleQueue<Contract> sq = new SimpleQueue<>();
        // 不够泛化 需要collection 即便有add接口也不适用该泛型化的接口
//        Fill.fill(sq, Contract.class, 5);

        // 适配器方式构造的泛化代码test Adapt a collection
        List<Coffee> carrier = new ArrayList<>();
        Fill2.fill(new AddableCollectionAdapter<>(carrier), Coffee.class, 5);
        Fill2.fill(Adapter.collectionAdapter(carrier), Latte.class, 5);
        for (Coffee coffee :carrier) {
            System.out.println(coffee.getClass().getName());
        }
        // it's ok
        AddableSimpleQueue<Coffee> addableSimpleQueue = new AddableSimpleQueue<>();
        Fill2.fill(addableSimpleQueue, Coffee.class, 5);
        Fill2.fill(addableSimpleQueue, Mocha.class, 5);
        Fill2.fill(addableSimpleQueue, Cappuccino.class, 5);

    }
}

interface Addable<T> {
    void add(T t);
}
class Fill2 {
    public static <T> void fill(Addable<T> addable, Class<? extends T> classToken, int size) {
        for (int i = 0; i < size; i++) {
            try {
                addable.add(classToken.newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    public static <T> void fill(Addable<T> addable, Generator<? extends T> generator, int size) {
        for (int i = 0; i < size; i++) {
            addable.add(generator.next());
        }
    }
}

/**
 * 为了适配基类型 需要使用组合将集合适用于此泛型
 */
class AddableCollectionAdapter<T> implements Addable<T> {

    private Collection<T> c;

    public AddableCollectionAdapter(Collection<T> c) {
        this.c = c;
    }

    @Override
    public void add(T t) {
        c.add(t);
    }
}

class Adapter {
    public static <T> Addable<T> collectionAdapter(Collection<T> c) {
        return new AddableCollectionAdapter<>(c);
    }
}

class AddableSimpleQueue<T> extends SimpleQueue<T> implements Addable<T> {
    @Override
    public void add(T t) {
        super.add(t);
    }
}
