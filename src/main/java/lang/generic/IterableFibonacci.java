package lang.generic;

import java.util.Iterator;

/**
 * 〈〉<br>
 *
 * @author Carrie
 * @create 2020/4/6
 * @since 1.0.0
 */
public class IterableFibonacci extends Fibonacci implements Iterable<Integer> {
    private int count = 0;

    public IterableFibonacci(int count) {
        this.count = count;
    }

    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            public boolean hasNext() {
                return count > 0;
            }

            public Integer next() {
                count--;
                return IterableFibonacci.this.next();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }

    public static void main(String[] args) {
        for (int i : new IterableFibonacci(10) ) {
            System.out.println(i);
        }
    }
}