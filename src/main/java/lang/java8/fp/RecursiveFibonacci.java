package lang.java8.fp;

/**
 * 〈斐波那契数列数列〉<br>
 *
 * @author Carrie
 * @create 2020/4/9
 * @since 1.0.0
 */
public class RecursiveFibonacci {
    IntCall fibonacci;

    RecursiveFibonacci() {
        fibonacci = n -> n == 0 ? 0 : n == 1 ? 1 : fibonacci.call(n - 1) + fibonacci.call(n - 2);
    }

    public static void main(String[] args) {
        RecursiveFibonacci recursiveFibonacci = new RecursiveFibonacci();
        System.out.println(recursiveFibonacci.fibonacci.call(10));
    }
}