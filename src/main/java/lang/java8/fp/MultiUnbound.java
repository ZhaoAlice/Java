package lang.java8.fp;

/**
 * 〈多个参数的非对象方法引用〉<br>
 *
 * @author Carrie
 * @create 2020/4/10
 * @since 1.0.0
 */
class MultiParam {
    private int ctor;
    String s;
    MultiParam() {
        this.ctor = 1;
    }
    MultiParam(int i) {
        this.ctor = i;
    }

    MultiParam(int i, String s) {
        this.ctor = i;
        this.s = s;
    }

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

interface Ctor0 {
    MultiParam ctor0();
}
interface Ctor1 {
    MultiParam ctor1(int i);
}
interface Ctor2 {
    MultiParam ctor2(int i, String s);
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

        // 编译器会根据构造函数与接口方法中的参数将正确的方法进行绑定 调用正确的构造函数创建对象
        Ctor0 ctor0 = MultiParam::new;
        Ctor1 ctor1 = MultiParam::new;
        Ctor2 ctor2 = MultiParam::new;

        // 利用构造函数引用来创建对象
        MultiParam param0 =  ctor0.ctor0();

        MultiParam param1 =  ctor1.ctor1(1);

        MultiParam param2 =  ctor2.ctor2(1, "this is me");

    }
}