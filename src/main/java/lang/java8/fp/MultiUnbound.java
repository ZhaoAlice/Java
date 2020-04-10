package lang.java8.fp;

/**
 * 〈多个参数的非对象方法引用〉<br>
 *
 * @author Carrie
 * @create 2020/4/10
 * @since 1.0.0
 */
class MultiParam {
    void twoParam(int i, double d) {
        System.out.println("two params");
    }

    void threeParams(int i, double d, String s) {
        System.out.println("three params");
    }
    void fourParams(int i, double d, String s, char c) {
        System.out.println("four params");
    }
}

interface Call2 {
    void call2(MultiParam multiParam, int i, double d);
}

interface Call3 {
    void call3(MultiParam multiParam, int i, double d, String s);
}
interface Call4 {
    void call4(MultiParam multiParam, int i, double d, String s, char c);
}
public class MultiUnbound {

    public static void main(String[] args) {
        // 未绑定对象的方法引用 第一个参数为绑定的对象
        Call2 call2 = MultiParam::twoParam;
        Call3 call3 = MultiParam::threeParams;
        Call4 call4 = MultiParam::fourParams;
        MultiParam mt = new MultiParam();
        call2.call2(mt, 1,2.0);
        call3.call3(mt, 1,2.0, "3");
        call4.call4(mt, 1,2.0, "4", 'c');


    }
}