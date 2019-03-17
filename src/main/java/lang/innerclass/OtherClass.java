package lang.innerclass;
//测试dotthis class的内部类访问权限，创建内部类实例
public class OtherClass {
    public static void main(String[] args) {
        DotThis dotThis = new DotThis();
        dotThis.getInner().getOuterClass().f();
        //dotThis.new Inner().getOuterClass().f();
        Parcle p = new Parcle();
        p.contents().value();
    }
}
