package lang.innerclass;

class Parcle {
    private class Pcontents implements Contents {
        private int i;
        public Pcontents(int value) {
            i = value;
        }
        @Override
        public int value() {
            return i;
        }
    }
    protected class Pdestination implements Destination {
        private String label;
        // 非静态内部类不允许有静态字段
//        private static int j = 0;
        public Pdestination(String label1) {
            label = label1;
        }
        @Override
        public String readLabel() {
            return label;
        }
        // 非静态内部类不允许有任何static的成员声明
//        public static void testStaticFun() {
//            return;
//        }
    }
    public Destination destination() {
        return new Pdestination("test intf");
    }
    public Contents contents() {

        return new Pcontents(7);
    }
    // 访问内部类的私有字段
    public int contents1() {

        return new Pcontents(7).i;
    }
}
class Warpping {
    private int value;
    public Warpping(int v) {
        value = v;
    }
    protected int readValue() {
        return value;
    }
}

public class Parcle4 {
    private Contents getContents(final int v) {
        //返回一个匿名内部类，不能有构造函数，且只能引用外部final修饰的参数
        return new Contents() {
            int value = v;
            @Override
            public int value() {
                return value;
            }
        };
    }
    private Warpping getWarpping(int x) {
        //通过父类的构造函数实现参数初始化
        return new Warpping(x) {
            @Override
            public int readValue() {
                return super.readValue();
            }
        };
    }
    public Destination getDestination(String to) {
        //定义在方法中内部类，不能使用访问控制标识符
        class PDestination1 implements Destination {
            private String label;
            private PDestination1(String lbl) {
                label = lbl;
            }
            @Override
            public String readLabel() {
                return label;
            }
        }
        return new PDestination1("testto");
    }
    static int test = 0;
    public static void main(String[] args) {
        Parcle p = new Parcle();
        Contents c = p.contents();
        c.value();
        Parcle4 p4 = new Parcle4();
        Destination d1 = p4.getDestination("readto");
        System.out.println(d1.readLabel());
        System.out.println(p4.getContents(9).value());
        System.out.println(p4.getWarpping(10).readValue());
        System.out.println(Parcle4.test);

    }
}