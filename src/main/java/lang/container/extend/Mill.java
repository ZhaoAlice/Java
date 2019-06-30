/**
 * Copyright (C), 2019
 * FileName: Mill
 * Author:   ZLG
 * Date:     2019/6/29 20:25
 * Description: 协变返回类型
 * History:
 * <author>          <time>          <version>          <desc>
 */
package lang.container.extend;

/**
 * 〈一句话功能简述〉<br> 
 * 〈协变返回类型〉
 *
 * @author ZLG
 * @create 2019/6/29
 * @since 1.0.0
 */
public class Mill {

    Fruit process() {
        return new Fruit(9);
    }
    Apple process1() {
        return new Apple("test");
    }
}