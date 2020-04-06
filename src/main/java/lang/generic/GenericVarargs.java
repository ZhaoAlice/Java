package lang.generic;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈可变参数与泛型方法〉<br>
 *
 * @author Carrie
 * @create 2020/4/6
 * @since 1.0.0
 */
public class GenericVarargs {
    public static <T> List<T> makeList(T... args) {
        ArrayList<T> list = new ArrayList<T>();
        for (T item : args) {
            list.add(item);
        }
        return list;
    }

    public static void main(String[] args) {
        String[] arg = "a b c d s e".split(" ");
        System.out.println(makeList(arg));
    }
}