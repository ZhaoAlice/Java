package lang.generic;

import com.sun.xml.internal.ws.policy.privateutil.RuntimePolicyUtilsException;

import java.util.Iterator;
import java.util.Random;

/**
 * 〈〉<br>
 *
 * @author Carrie
 * @create 2020/4/6
 * @since 1.0.0
 */
public class CoffeeGenerator implements Generator<Coffee>, Iterable<Coffee> {

    private Class[] types = {Latte.class, Cappuccino.class, Mocha.class};
    private static Random random = new Random(47);
    private int size = 0;
    public CoffeeGenerator() {}
    public CoffeeGenerator(int size) {
        this.size = size;
    }
    @Override
    public Coffee next() {
        try {
            return (Coffee) types[random.nextInt(types.length)].newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException();
        }
    }
    class CoffeeIterator implements Iterator<Coffee> {
        int count = size;

        @Override
        public boolean hasNext() {
            return count > 0;
        }

        @Override
        public Coffee next() {
            count--;
            return CoffeeGenerator.this.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    @Override
    public Iterator<Coffee> iterator() {
        return new CoffeeIterator();
    }

    public static void main(String[] args) {
        CoffeeGenerator generator = new CoffeeGenerator();
        for (int i = 0; i < 3; i++) {
            System.out.println(generator.next());
        }
        for (Coffee coffee : new CoffeeGenerator(3)) {
            System.out.println(coffee);
        }
    }
}