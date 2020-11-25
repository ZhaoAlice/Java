/**
 * Copyright (C), 2019
 * FileName: Fruit
 * Author:   ZLG
 * Date:     2019/6/29 18:57
 * Description: base class
 * History:
 * <author>          <time>          <version>          <desc>
 */
package lang.container.extend;

import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈base class〉
 *
 * @author ZLG
 * @create 2019/6/29
 * @since 1.0.0
 */
public class Fruit {

    private String title = "parent class title";

    /**
     * 不会覆盖父类的内容 父类与子类相同名称的field分别存储在不同的空间
      */
    private Map<String, String> sunshine;

    private static int j = 10;
    private int i = 9;

    public Fruit(int j) {
        this.sunshine = new HashMap<String, String>();
        System.out.println(i);
        this.i = j;
    }
    private final void printClassName() {
        System.out.println("base class");
        System.out.println(this.getClass().getName());
    }
    public final void printClassName1() {
        System.out.println("base class");
        System.out.println(this.getClass().getName());
    }

    public String getSunshine() {
        return sunshine.get("subclass");

    }
    public String getTitle() {
        return title;
    }

}