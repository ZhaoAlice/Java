package lang.java15;

import org.junit.Test;

/**
 * 〈〉<br>
 *
 * @author Carrie
 * @create 2022/9/13
 * @since 1.0.0
 */
public class TestFeature {

    private String name;

    @Test
    public void testTextBlock(Object e) {
        // 判断实例的类型时，直接创建临时变量，便于后面引用，不需要再进行强制转换
        if (e instanceof lang.java15.TestFeature testFeature) {
            System.out.println(testFeature.name);
        }

    }
}