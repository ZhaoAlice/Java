package lang.generic;

import com.sun.org.apache.bcel.internal.generic.NEW;
import lang.container.collection.List;

import java.util.AbstractList;

/**
 * 〈通配符〉<br>
 *
 * @author Carrie
 * @create 2020/4/19
 * @since 1.0.0
 */
class Fruit {

}

class Apple extends Fruit {

}

class orange extends Fruit {

}

class Jonathan extends Apple {

}

class NonCovariantGenerics {
    /**
     * 与数组不同 泛型将类型检查移入编译期
     * 不能将一个涉及apple的泛型赋值给一个涉及到fruit的泛型
     * Apple的list在类型上不等于Fruit的list
     * 真正的问题是我们在谈论容器的类型而不是容器持有的类型
     * 与数组不同 泛型没有内建的协变类型 因为数组在语言中是完全定义的 因此可以内建编译期和运行时检查
     * 但是使用泛型时 编译器和运行时系统都不知道你想用类型做些什么以及应该采用什么样的规则
     *
     */
//    List<Fruit> fruitList = new AbstractList<Apple>();
}
public class CovariantArrays {
    public static void main(String[] args) {
        // 实际运行时类型是apple 你只能往里面放apple或者其子类型对应的实例
        Fruit[] fruit = new Apple[10];

        // 编译期是允许的 因为本身就是fruit引用 运行时就要报错了
        fruit[0] = new Fruit();
        fruit[1] = new Apple();
        fruit[2] = new Jonathan();
        // 编译期是允许的 因为本身就是fruit引用 运行时就要报错了
        fruit[3] = new orange();
    }

}