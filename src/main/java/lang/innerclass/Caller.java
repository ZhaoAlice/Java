package lang.innerclass;

interface incretmentable {
    void increment();
}
class Caller1 implements incretmentable {
        private int i = 0;
    @Override
    public void increment() {
        i++;
        System.out.println(i);
    }
}
class MyIncretment implements incretmentable {

    @Override
    public void increment() {
        System.out.println("output value");
    }
    static void f(MyIncretment mi) {
        mi.increment();
    }
}
class Caller2 extends MyIncretment implements incretmentable {
    private int i = 0;
    @Override
    public void increment() {
        super.increment();
        i++;
        System.out.println(i);
    }
}
class CallBackRef {
    private incretmentable intf;
    public CallBackRef(incretmentable inc) {
        intf = inc;
    }
    public void go() {
        intf.increment();
    }
}

public class Caller {
    public static void main(String[] args) {
        Caller1 c1 = new Caller1();
        Caller2 c2 = new Caller2();
        MyIncretment.f(c2);
        CallBackRef cref1 = new CallBackRef(c1);
        CallBackRef cref2 = new CallBackRef(c2);
        cref1.go();
        cref1.go();
        cref2.go();
        cref2.go();
    }
}