/**
 * Copyright (C), 2019
 * FileName: RandomDoublesAdapter
 * Author:   ZLG
 * Date:     2019/6/30 16:55
 * Description: Make class RandomDoubles to be a parameter for Scanner
 * History:
 * <author>          <time>          <version>          <desc>
 */
package lang.interfaces;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Scanner;

/**
 * 〈一句话功能简述〉<br> 
 * 〈Make class {@link RandomDoubles} to be a parameter for {@link java.util.Scanner}〉
 *
 * @author ZLG
 * @create 2019/6/30
 * @since 1.0.0
 */
public class RandomDoublesAdapter extends RandomDoubles implements Readable {
    private int count;

    public RandomDoublesAdapter(int count) {
        this.count = count;
    }

    @Override
    public int read(CharBuffer cb) throws IOException {
        if (count-- == 0) {
            return -1;
        }
        String result = Double.toString(next()) + " ";
        cb.append(result);
        System.out.println(result.length());
        return result.length();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(new RandomDoublesAdapter(10));
        while (scanner.hasNextDouble()) {
            System.out.println(scanner.nextDouble() + " ");
        }
    }
}