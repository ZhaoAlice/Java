package lang.java13;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.Locale;

import org.junit.Test;

import lang.container.extend.Fruit;

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
        // jdk13 yield
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

    @Test
    public void testTextBlocks() {
        String text = "text block\n" +
            "text block";
        // 前面的三个双引号必须换行 否则不符合语法
        // 后边的三个双引号换行的的话 字符串的长度为多1, 不换行 则字符串的长度为实际长度
        String textBlock = """
            text block
            text block""";
        System.out.println(text);
        System.out.println(textBlock);
        System.out.println(text.length());
        System.out.println(textBlock.length());
    }

    enum Fruit {
        APPLE, ORANGE, BANANA, PEAR, PEACH, WATER_MELON, KIWI_FRUIT
    }
}