package lang.java8.streams;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 〈集合生成流〉<br>
 *
 * @author Carrie
 * @create 2020/4/16
 * @since 1.0.0
 */
class S {
    public int getI() {
        return i;
    }

    private int i;

    public S(int i) {
        this.i = i;
    }

    public String toString() {
        System.out.println("S");
        return Integer.valueOf(i).toString();
    }
}

// 集合生成流
public class CollectionStream {
    public static void main(String[] args) {
        List<S> sList = Arrays.asList(new S(1), new S(2), new S(3));
        System.out.println(sList.stream()
                .mapToInt(s -> s.getI())
                .sum());
        Set<String> w = new HashSet<>(Arrays.asList("it's a wonderful day for pie!".split(" ")));
        // 将集合创建为流 后续的操作基于流进行
        w.stream()
                // map通常会获取一个对象 然后产生一个新对象
                .map(x -> x + " ")
                .forEach(System.out::print);
        System.out.println();

        Map<String, Double> m = new HashMap<>();
        m.put("pi", 3.14159);
        m.put("e", 2.718);
        m.put("phi", 1.618);
        m.entrySet().stream()
                .map(e -> e.getKey() + ":" + e.getValue())
                .forEach(System.out::print);

    }

}