package lang.java8.fp;

/**
 * 〈使用lambda表达式时 递归方法必须是实例变量或者是静态变量〉<br>
 *
 * @author Carrie
 * @create 2020/4/9
 * @since 1.0.0
 */
public class RecursiveFactorial {
    // n的阶乘
    static IntCall fact;

    public static void main(String[] args) {
        fact = (n) -> n == 0 ? 1 : n * fact.call(n-1);
        for (int i = 0; i < 10; i++) {
            System.out.println(fact.call(i));
        }
    }
}