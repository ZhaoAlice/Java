package lang.generic;

import java.util.List;

/**
 * 〈超类型通配符 可以声明通配符是由某个特定类的任何基类来界定的 还可以使用类型参数〉<br>
 *  你可以安全的传递一个类型对象对泛型类型中
 *
 * @author Carrie
 * @create 2020/5/10
 * @since 1.0.0
 */
public class SuperTypeWildcards {
    /**
     * 参数apples是Apple某种基类型的list
     * @param apples
     */
    static void writeTo(List<? super Apple> apples) {
        apples.add(new Apple());
        apples.add(new Jonathan());
        // 只能是apple及其子类型 apple是下界 如果塞入基类 编译器又要蒙了
//        apples.add(new Fruit());
    }
}