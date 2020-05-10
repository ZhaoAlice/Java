package lang.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class UnboundedWildcards2 {
    static Map map1;
    static Map<?, ?> map2;
    static Map<String, ?> map3;

    static void assgin1(Map map) {
        map1 = map;
    }

    static void assgin2(Map<?, ?> map) {
        map2 = map;
    }
    static void assgin3(Map<String, ?> map) {
        map3 = map;
    }

    public static void main(String[] args) {
        assgin1(new HashMap());
        assgin2(new HashMap());
        assgin3(new HashMap());

        assgin1(new HashMap<String, Integer>());
        assgin2(new HashMap<String, Integer>());
        assgin3(new HashMap<String, Integer>());

    }
}
/**
 * 〈无界通配符?〉<br>
 *
 * @author Carrie
 * @create 2020/5/10
 * @since 1.0.0
 */
public class UnboundedWildcard {
    // 而list实际上也是List<Object> 表示持有任何Object类型的原生List
    static List list1;
    /**
     * list<?> 表示具有某种特定类型的非原生list 只是我们不知道那种类型是什么
     */
    static List<?> list2;
    static List<? extends Object> list3;

    static void assign1(List list) {
        list1 = list;
        list2 = list;
        list3 = list;
    }
    static void assign2(List<? extends Object> list) {
        list1 = list;
        list2 = list;
        list3 = list;
    }
    static void assign3(List<?> list) {
        list1 = list;
        list2 = list;
        list3 = list;
    }

    public static void main(String[] args) {
        assign1(new ArrayList());
        assign2(new ArrayList());
        assign3(new ArrayList());

        List<?> list = new ArrayList();
        List<? extends Object> list1 = new ArrayList();

        assign1(new ArrayList<String>());
        assign2(new ArrayList<String>());
        assign3(new ArrayList<String>());

        List<?> wildList = new ArrayList();
        wildList = new ArrayList<String>();
        // 我想用java的泛型来编写这些代码 我在这里并不是要用原生类型 但是在当前这种情况下
        // 泛型参数可以持有任何类型
        assign1(wildList);
        assign2(wildList);
        assign3(wildList);
    }
}