package lang.java11;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 〈〉<br>
 *
 * @author Carrie
 * @create 2022/9/8
 * @since 1.0.0
 */
public class CollectionFeature {
    public static void main(String[] args) {
        // 集合转数组
        List<String> stringList = Arrays.asList("aa", "bb");
        String[] strArr = stringList.toArray(String[]::new);

        // 0
        Stream.ofNullable(null).count();

        // dropWhile  当不满足条件时 则跳出while循环 结果为(从不满足的条件元素开始->end]
        Stream.of(1, 2, 5, 4, 3, 2, 2, 1)
            .dropWhile(n -> n < 3)
            .collect(Collectors.toList())
            .forEach(integer -> System.out.println(integer));

        // takeWhile 当不满足条件时 则跳出while循环 结果为[start -> 不满足的条件元素)
        Stream.of(1, 2, 3, 0, 2, 1)
            .takeWhile(n -> n < 3)
            .collect(Collectors.toList())
            .forEach(integer -> System.out.println(integer));

        Optional.of("optional").orElseThrow();
        // 转换为stream
        Optional.of("optional").stream().count();
        Optional.ofNullable(null).or(() -> Optional.of("other optional"))
            .get();
    }
}