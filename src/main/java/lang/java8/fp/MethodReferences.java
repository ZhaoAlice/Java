package lang.java8.fp;

/**
 * 〈方法引用〉<br>
 *
 * @author Carrie
 * @create 2020/4/9
 * @since 1.0.0
 */
interface Callable {
    void call(String s);
}

class Descriptions {
    void show(String msg) {
        System.out.println(msg);
    }
}

public class MethodReferences {

    static void hello(String msg) {
        System.out.println("hello, " + msg);
    }

    static class Descrption1 {
        String about;

        Descrption1(String about) {
            this.about = about;
        }

        void help(String msg) {
            System.out.println(about + " " + msg);
        }
    }

    static class Helper {
        static void assist(String msg) {
            System.out.println( msg);
        }
    }

    public static void main(String[] args) {
        Descriptions descriptions = new Descriptions();
        // 实例方法
        Callable callable = descriptions :: show;
        callable.call("decription show");
        // 静态方法
        callable = MethodReferences :: hello;
        callable.call("static method");

        // 绑定实例的方法
        callable = new Descrption1("instance method")::help;
        callable.call("aa");

        // 绑定不同的方法名 静态内部类的静态方法引用
        callable = Helper::assist;
        callable.call("different method name");
    }
}