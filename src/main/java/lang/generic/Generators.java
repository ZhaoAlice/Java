package lang.generic;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 〈生成器〉<br>
 *
 * @author Carrie
 * @create 2020/4/6
 * @since 1.0.0
 */
public class Generators {
    public static <T> Collection<T> fill(Collection<T> coll, Generator<T> generator, int n) {
        for (int i = 0; i < n; i++) {
            coll.add(generator.next());
        }
        return coll;
    }

    public static void main(String[] args) {
        ArrayList<Coffee> coffees = new ArrayList<Coffee>();
        fill(coffees, new CoffeeGenerator(), 3);
        for (Coffee coffee : coffees) {
            System.out.println(coffee);
        }
        ArrayList<Integer> fibonaccis = new ArrayList<Integer>();
        fill(fibonaccis, new Fibonacci(), 12);
        for (int i : fibonaccis) {
            System.out.println(i);
        }
    }
}