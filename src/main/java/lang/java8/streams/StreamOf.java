package lang.java8.streams;

import lang.java8.fp.IntCall;
import lang.java8.fp.TransformFunction;

import java.util.stream.Stream;

/**
 * 〈流的创建〉<br>
 *
 * @author Carrie
 * @create 2020/4/15
 * @since 1.0.0
 */
public class StreamOf {
    public static void main(String[] args) {
        TransformFunction tf = new TransformFunction();
        IntCall intCall1 = tf.getI();
        IntCall intCall2 = tf.getI();
        IntCall intCall3 = tf.getI();

        // 生成流的时候第一个元素类型必须是有权限访问的后面的元素则根据第一个元素进行推断
        // 如果写成Stream.of(tf.getI(), tf.getI(), tf.getI())则会报错 因为I是包访问权限
        Stream.of(intCall1, tf.getI(), tf.getI()).forEach(System.out::println);
    }
}