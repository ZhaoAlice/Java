package lang.generic;

import java.util.ArrayList;
import java.util.Random;

/**
 * 〈泛型学习〉<br>
 *
 * @author Carrie
 * @create 2020/4/6
 * @since 1.0.0
 */
public class RandomList<T> {
    private ArrayList<T> storage = new ArrayList<T>();
    private Random rand = new Random();

    public void add(T item) {
        storage.add(item);
    }

    public T select() {
        return storage.get(rand.nextInt(storage.size()));
    }

    public static void main(String[] args) {
        RandomList<String> randomList = new RandomList<String>();
        for (String s : "a b c d e".split(" ")) {
            randomList.add(s);
        }
        for (int i = 0; i < 5; i++) {
            System.out.println(randomList.select());
        }
    }
}