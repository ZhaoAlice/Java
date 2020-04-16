package lang.java8.streams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 〈随机单词〉<br>
 *
 * @author Carrie
 * @create 2020/4/16
 * @since 1.0.0
 */
public class RandomWords implements Supplier<String> {

    List<String> words = new ArrayList<>();
    Random rand = new Random(47);

    @Override
    public String get() {
        return words.get(rand.nextInt(words.size()));
    }


    RandomWords(String fileName) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        for (String line : lines.subList(1, lines.size())) {
            for (String s : line.split("[ .?,]+")) {
                words.add(s);
            }

        }
    }

    @Override
    public String toString() {
        return words.stream().collect(Collectors.joining(" "));
    }

    public static void main(String[] args) throws Exception {
        System.out.println(Stream.generate(new RandomWords("D:\\AllCheckedFromGit\\github\\Java\\src\\main\\\\resources\\resource\\cheese.dat"))
        .limit(10)
        .collect(Collectors.joining(" ")));
    }
}