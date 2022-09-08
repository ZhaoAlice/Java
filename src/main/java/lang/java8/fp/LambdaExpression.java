package lang.java8.fp;

/**
 * 〈lambda 函数式编程要求所有的接口只能有一个方法〉<br>
 *
 * @author Carrier
 * @create 2020/4/9
 * @since 1.0.0
 */
interface Description {
    String brief();
}

interface Body {
    String detail(String head);
}

interface Multi {
    String twoArg(String head, Double d);
}

public class LambdaExpression {
    // 参数可以带括号 也可以不带
    static Body body = head -> head + " No head!";
    static Body body1 = (head) -> head + " two head";

    // 无参的方法则
    static Description description = () -> "brief";

    static Multi multi = (h, d) -> h + d;

    static Description morelines = () -> {
        System.out.println("multi line need 大括号");
        // 如果方法有返回值 则需要添加上return
        return "more lines";
    };

    public static void main(String[] args) {
        System.out.println(body.detail("head"));
        System.out.println(body1.detail("head wit ()"));
        System.out.println(description.brief());
        System.out.println(morelines.brief());
        System.out.println(multi.twoArg("two arg", 2.0));
    }
}