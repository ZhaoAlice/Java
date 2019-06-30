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

/**
 * 〈一句话功能简述〉<br> 
 * 〈base class〉
 *
 * @author ZLG
 * @create 2019/6/29
 * @since 1.0.0
 */
public class Fruit {

    private static int j = 10;
    private int i = 9;
    public Fruit(int j) {
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
}