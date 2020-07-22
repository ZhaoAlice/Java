package lang.java8.streams;

import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 〈生成器〉<br>
 *
 * @author Carrie
 * @create 2020/4/20
 * @since 1.0.0
 */
public class Generators implements Supplier<String> {
    Random rand = new Random(47);
    char[] letters = "ABCDEFGHIJKLMN".toCharArray();
    
    @Override
    public String get() {
        return "" + letters[rand.nextInt(letters.length)];
    }

    public static void main(String[] args) {
        // 使用supplier生成流元素
        String words = Stream.generate(new Generators())
                .limit(30)
                .collect(Collectors.joining());
        System.out.println(words);
    }
}