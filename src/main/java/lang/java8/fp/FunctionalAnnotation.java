package lang.java8.fp;

/**
 * 〈函数式注解〉<br>
 *
 * @author Carrie
 * @create 2020/4/11
 * @since 1.0.0
 */
@FunctionalInterface
interface Functional {
    String say(String msg);
}
interface NoAnnoFunctional {
    String say(String msg);
}
public class FunctionalAnnotation {
    public String learn(String content) {
        return content;
    }
    public static void main(String[] args) {
        FunctionalAnnotation fa = new FunctionalAnnotation();
        Functional f = fa::learn;
        f.say("I like programing");
        NoAnnoFunctional nf = fa::learn;
        nf.say("I like computer!");
        Functional f1 = (msg) -> msg;
        f1.say("I want to go to school!");

    }
}