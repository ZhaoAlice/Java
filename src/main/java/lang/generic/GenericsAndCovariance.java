package lang.generic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 〈通配符〉<br>
 *
 * @author Carrie
 * @create 2020/4/23
 * @since 1.0.0
 */

public class GenericsAndCovariance {

    public static void main(String[] args) {
        List<? extends Fruit> flist = new ArrayList<>();
        List<? extends Fruit> flist1 = new ArrayList<Apple>();
//        flist1.add(apple);

        Apple apple = new Apple();
        List<? extends Fruit> flist2 = Arrays.asList(apple);

//        flist2.add(apple); // add添加的是参数类型 无法确定
        Apple apple1 = (Apple) flist2.get(0);
        System.out.println(flist2.contains(apple));
        System.out.println(flist2.indexOf(apple));
        // 编译器无法确定容器想要持有哪种确定的类型 只知道是fruit或其子类型
//        flist.add(new Apple());
//        flist.add(new Fruit());
//        flist.add(new Object());
        flist.add(null);
        Fruit fruit = flist.get(0);
    }
}