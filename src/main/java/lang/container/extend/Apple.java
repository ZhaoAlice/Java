/**
 * Copyright (C), 2019
 * FileName: apple
 * Author:   ZLG
 * Date:     2019/6/29 18:58
 * Description: Sub class
 * History:
 * <author>          <time>          <version>          <desc>
 */
package lang.container.extend;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 基类<br>
 * 〈Sub class〉
 *
 * @author ZLG
 * @create 2019/6/29
 * @since 1.0.0
 */
public class Apple extends Fruit  {

    private Map<String, String> sunshine;

    @Override
    public String getTitle() {
        // 取到的是父类的title
        return super.getTitle();
    }

    private String title = "subclass";

    private static Random random = new Random();
    // OK 运行时常量
    private static final int SV = random.nextInt();
    // OK
    private final int iv1 = random.nextInt();
    private Random random1 = new Random();
    // be not allowed
//    private static final int j = random1.nextInt();
    // OK 编译期常量,只针对基本类型
    private final int iv = random1.nextInt();
    private static int h = 11;
    private String str = "str";
    public Apple(String str) {
        super(10);
        System.out.println(str);
        this.str = str;
        this.sunshine = new HashMap<String, String>();
    }
    public final void printClassName() {
        System.out.println(this.getClass().getName());
    }
    @Override
    public String getSunshine() {
        System.out.println(getTitle());
        // 取到是父类的sunshine
        return super.getSunshine();

    }
    public static void main(String[] args) {
        Apple apple = new Apple("apple");
        apple.printClassName();
        apple.sunshine.put("subclass", "sunshine");
        System.out.println(apple.getSunshine());
    }
    // 不允许覆盖基类中作为接口的final方法
//    public final void printClassName1() {
//        System.out.println("base class");
//        System.out.println(this.getClass().getName());
//    }
}