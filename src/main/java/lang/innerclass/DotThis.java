package lang.innerclass;

//生成外部类对象的引用，outclass.this,创建内部类对象必须使用外部类对象，而不能想当然的认为使用外部类.内部类()
public class DotThis {
    void f() {
        System.out.println("DotThis.f()");
    }
    class Inner {
       public DotThis getOuterClass() {
            return DotThis.this;
        }
    }
    public Inner getInner() {
        return new Inner();
    }
    public static void main(String[] args) {
        // 调用外部类的方法，生成内部类
        DotThis.Inner inner =  new DotThis().getInner();
        // 获取外部类对象的引用，并调用其方法，(外部类.this)
        inner.getOuterClass().f();
        // 使用外部类对象.new 内部类 创建内部类
        DotThis.Inner inner1 = new DotThis().new Inner();
    }
}
