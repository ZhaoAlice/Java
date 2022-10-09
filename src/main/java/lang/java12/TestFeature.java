package lang.java12;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.Locale;

import org.junit.Test;

/**
 * 〈〉<br>
 *
 * @author Carrie
 * @create 2022/9/13
 * @since 1.0.0
 */
public class TestFeature {
    /**
     * 可以不需要break语句 新的语法只会执行匹配的语句 无穿透效应
     */
    @Test
    public void testSwitch() {
        Fruit fruit = Fruit.KIWI_FRUIT;
        switch (fruit) {
            case KIWI_FRUIT, PEAR -> System.out.println("apple pear");
            default -> throw new IllegalArgumentException();
        }
        // 使用switch语句给某个变量赋值
        int result = switch (fruit) {
            case BANANA, PEACH -> "banana peach".length();
            default -> {
                int code = fruit.hashCode();
                // 控制权交给switch外部 return不符合语法
                yield code;
            }
        };
        System.out.println(result);
    }

    /**
     * transform 对字符串做转换
     * indent 缩进
     */
    @Test
    public void testString() {
        String value = "hello";
        String concatVal = value
            .transform(val -> val + " world")
            .transform(String::toUpperCase)
            .indent(3);
        System.out.println(concatVal);

    }

    /**
     * Files.mismatch 文件内容是否匹配
     * @throws IOException
     */
    @Test
    public void testFiles() throws IOException {
        try (FileWriter fileWriter = new FileWriter("tmp\\a.txt");
             FileWriter fileWriter1 = new FileWriter("tmp\\b.txt")) {
            fileWriter.write("a");
            fileWriter.write("b");
            fileWriter.write("c");
            fileWriter1.write("a");
            fileWriter1.write("b");
            fileWriter1.write("c");
            System.out.println(Files.mismatch(Path.of("tmp\\a.txt"), Path.of("tmp\\b.txt")));
        }
    }
    @Test
    public void testCompactNumberFormat() {
        var sformat = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
        var lformat = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.LONG);

        System.out.println(sformat.format(10000));
        System.out.println(sformat.format(100));
        System.out.println(sformat.format(1_100000));
        System.out.println(sformat.format(10000000));

        System.out.println(lformat.format(10000));
        System.out.println(lformat.format(100));
        System.out.println(lformat.format(1_100000));
        System.out.println(lformat.format(10000000));
    }

    enum Fruit {
        APPLE, ORANGE, BANANA, PEAR, PEACH, WATER_MELON, KIWI_FRUIT
    }
}