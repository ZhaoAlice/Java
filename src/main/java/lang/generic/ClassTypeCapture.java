package lang.generic;

/**
 * 〈类型补偿〉<br>
 * 擦除丢失了在泛型代码中执行某些操作的能力 任何在运行时需要知道确切类型信息的操作都将无法工作
 * 偶尔可以绕过这些问题来编程 但是有时必须通过引入类型标签来对擦除进行补偿
 *
 * @author Carrie
 * @create 2020/5/9
 * @since 1.0.0
 */
class Building {

}
class House extends Building {}
public class ClassTypeCapture<T> {
    Class<T> kind;

    public ClassTypeCapture(Class<T> kind) {
        this.kind = kind;
    }

    public boolean f(Object arg) {
        return kind.isInstance(arg);
    }

    /**
     * 编译器将确保类型标签可以匹配泛型参数
     * @param args
     */
    public static void main(String[] args) {
        ClassTypeCapture<Building> ctt1 = new ClassTypeCapture<>(Building.class);
        System.out.println(ctt1.f(new Building()));
        System.out.println(ctt1.f(new House()));
        ClassTypeCapture<House> ctt2 = new ClassTypeCapture<>(House.class);
        System.out.println(ctt2.f(new Building()));
        System.out.println(ctt2.f(new House()));
    }
}