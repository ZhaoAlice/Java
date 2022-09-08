package lang.java11;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 〈〉<br>
 *
 * @author Carrie
 * @create 2022/9/8
 * @since 1.0.0
 */
public class VarPredicateFeature {
    public static void main(String[] args) {
        List<String> stringList = Arrays.asList("xiangliu", "xiaoyao", "tushanjing", "cangxuan");
        List<String> result = stringList.stream()
            // 如果加了var 则可以添加方法参数支持的特性
            .filter((var s) -> s.startsWith("z"))
            // 断言取反 与 !效果一样
            .filter(Predicate.not(s -> s.contains("z")))
            .collect(Collectors.toList());
    }
}