package lang.java8.fp;

/**
 * 〈〉<br>
 *
 * @author Carrie
 * @create 2020/4/8
 * @since 1.0.0
 */
interface Strategy {
    String approach(String msg);
}
class Soft implements Strategy {

    @Override
    public String approach(String msg) {
        return msg.toLowerCase() + "?";
    }
}
class Unrelated {
    static String twice(String msg) {
        return msg + " " + msg;
    }
}
public class Strategize {
    Strategy strategy;
    String msg;
    Strategize(String msg) {
        this.strategy = new Soft();
        this.msg = msg;
    }

    void communicate() {
        System.out.println(strategy.approach(msg));
    }

    void changeStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public static void main(String[] args) {
        Strategy[] strategies = {
                // 匿名内部类
                new Strategy() {
                    @Override
                    public String approach(String msg) {
                        return msg.toUpperCase() + "!";
                    }
                },
                // lambda表达式 箭头左边是参数 右面是函数体
                // 这种适用于类或接口中的lambda表达式 只能有一个方法 才能使用函数式编程
                // 更多行的时候使用大括号括起来
                msg -> msg.substring(0, 5),
                // 左边是类或者对象 右边是方法 没有参数
                Unrelated::twice
        };

        Strategize strategize = new Strategize("hello world");
        strategize.communicate();
        for (Strategy strategy : strategies) {
            strategize.changeStrategy(strategy);
            strategize.communicate();
        }
    }
}