/**
 * Copyright (C), 2019
 * FileName: WheatMill
 * Author:   ZLG
 * Date:     2019/6/29 20:27
 * Description: sub class
 * History:
 * <author>          <time>          <version>          <desc>
 */
package lang.container.extend;

/**
 * 〈一句话功能简述〉<br> 
 * 〈sub class〉
 *
 * @author ZLG
 * @create 2019/6/29
 * @since 1.0.0
 */
public class WheatMill extends Mill {

    @Override
    Apple process() {
        return new Apple("test");
    }
    // 协变返回类型只应用于方法返回类型非参数类型，只能返回父类中该方法返回类型的派生类类型，反过来则不行
//    Fruit process1() {
//        return new Fruit("test");
//    }
}