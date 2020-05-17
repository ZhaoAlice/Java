package lang.generic;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

class Shape {
    public void rotate() {
        System.out.println(this + " rotate");
    }

    public void resize(int newSize) {
        System.out.println(this + " resize" + newSize);
    }
}
class Square extends Shape{

}

class FilledList<T> extends ArrayList<T> {
    public FilledList(Class<? extends T> type, int size) {
        try {
            for (int i = 0; i < size; i++) {
                add(type.newInstance());
            }
        } catch (Exception e) {
            throw new  RuntimeException();
        }
    }
}
/**
 * 〈潜在类型机制补偿〉<br>
 *
 * @author Carrie
 * @create 2020/5/17
 * @since 1.0.0
 */
public class Apply {
    public static <T, S extends Iterable<? extends T>> void apply(S seq, Method f, Object ...args) {
        try {
            for (T t : seq) {
                f.invoke(t, args);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class SimpleQueue<T> implements Iterable<T> {
    private LinkedList<T> list = new LinkedList<>();
    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    public void add(T t) {
        list.offer(t);
    }

    public T get() {
        return list.poll();
    }

}
class ApplyTest {
    /**
     * 通过反射将接口应用于某一类对象
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        List<Shape> shapes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            shapes.add(new Shape());
        }
        Apply.apply(shapes, Shape.class.getMethod("rotate"));
        Apply.apply(shapes, Shape.class.getMethod("resize", int.class), 5);

        List<Square> squares = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            squares.add(new Square());
        }
        Apply.apply(squares, Square.class.getMethod("rotate"));
        Apply.apply(squares, Square.class.getMethod("rotate", int.class), 5);

        Apply.apply(new FilledList<>(Shape.class, 5), Shape.class.getMethod("resize", int.class), 10);

        SimpleQueue<Shape> shapes1 = new SimpleQueue<>();
        for (int i = 0; i < 5; i++) {
            shapes1.add(new Shape());
            shapes1.add(new Square());
        }
        Apply.apply(shapes1, Shape.class.getMethod("resize", int.class), 5);
    }
}