package lang.java8.streams;

import javax.sound.midi.Soundbank;

import java.util.stream.LongStream;

import static java.util.stream.LongStream.*;

/**
 * 〈移除元素 distinct filter〉<br>
 *
 * @author Carrie
 * @create 2020/7/22
 * @since 1.0.0
 */
public class Prime {

    public static boolean isPrime(long n) {
        // 判断是否是质数 素数 除1和本身 不能被其他数整除
        return rangeClosed(2, (long) Math.sqrt(n))
                .noneMatch(i -> n % i == 0);
    }
    public LongStream numbers() {
        // iterate 以种子的第一个参数开头 并将其传给方法 方法的结果将存储到流中 并且作为方法的第一个参数 继续下一轮的迭代
        return iterate(2, i -> i + 1)
                .filter(Prime::isPrime);
    }
    public static void main(String[] args) {

        new Prime().numbers()
                .limit(10)
                .forEach(n -> System.out.format("%d ", n));
        System.out.println();
        new Prime().numbers().skip(90).limit(10)
                .forEach(n -> System.out.format("%d ",n));
    }
}