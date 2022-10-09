package lang.java14;

/**
 * 〈该语法会反编译为class 类似lombok插件 感觉用处不大〉<br>
 *  括号中的参数为类的实例变量 会生成与参数个数相同的构造函数
 *  示例变量的get方法（方法名称为实例变量的名称）
 *  equals toString hashCode方法
 *  不允许声明为抽象类 如 public abstract record RecordFeature
 *  不可以显示的再继承于其他类 如 record RecordFeature(String name, String con) extends OtherClass
 *
 * @author Carrie
 * @create 2022/9/14
 * @since 1.0.0
 */
public record RecordFeature(String name, String con) {

    // 不允许再定义额外的实例变量
    //private String notAllowed;

    // 允许静态变量
    private static String allowed;

    /**
     * 反编译后的代码
     */
    public final class RecordFeature1 {
        private final String name;
        private final String con;

        public RecordFeature1(String name, String con) {
            this.name = name;
            this.con = con;
        }

        public String name() {
            return this.name;
        }

        public String con() {
            return this.con;
        }
    }
}


