/**
 * Copyright (C), 2019
 * FileName: RandomDoubles
 * Author:   ZLG
 * Date:     2019/6/30 16:52
 * Description: generate random doule number
 * History:
 * <author>          <time>          <version>          <desc>
 */
package lang.interfaces;

import java.util.Random;

/**
 * 〈一句话功能简述〉<br> 
 * 〈generate random doule number〉
 *
 * @author ZLG
 * @create 2019/6/30
 * @since 1.0.0
 */
public class RandomDoubles {

    public Random random = new Random(47);
    public double next() {
        return random.nextDouble();
    }

    public static void main(String[] args) {
        RandomDoubles randomDoubles = new RandomDoubles();
        for (int i = 0; i < 10; i++) {
            System.out.println(randomDoubles.next());
        }
    }
}