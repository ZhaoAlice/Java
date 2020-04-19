package lang.generic;

import com.sun.org.apache.bcel.internal.generic.NEW;

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
public class CovariantArrays {
    public static void main(String[] args) {
        Fruit[] fruit = new Apple[10];

    }

}