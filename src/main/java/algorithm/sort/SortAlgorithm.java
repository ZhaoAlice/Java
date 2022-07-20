package algorithm.sort;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 〈〉<br>
 *
 * @author Carrie
 * @create 2022/2/9
 * @since 1.0.0
 */
public class SortAlgorithm {
    static Logger logger = LoggerFactory.getLogger(SortAlgorithm.class);

    private static Random random = new Random();

    public static int[] sortByBubble(int[] waitForSort) {
        for (int i = 1; i < waitForSort.length; i++) {
            for (int j = waitForSort.length - 1; j >= i; j--) {
                if (waitForSort[j-1] > waitForSort[j]) {
                    int temp = waitForSort[j];
                    waitForSort[j] = waitForSort[j-1];
                    waitForSort[j-1] = temp;
                }
            }
        }
        return waitForSort;
    }

    public static int[] sortByInsert() {
        return null;
    }

    public static int[] quickSort(int[] waitForSort) {
        if (null == waitForSort) {
            return null;
        }
        return null;
    }

    public static String add(String numberA, String numberB) {
        if (numberA == null && numberB == null) {
            return null;
        }
        if (numberA == null) {
            return numberB;
        }
        if (numberB == null) {
            return numberA;
        }
        int aPosition = numberA.length();
        int bPosition = numberB.length();
        boolean carryFlag = false;
        StringBuilder stringBuilder = new StringBuilder((aPosition > bPosition ? aPosition : bPosition) + 1);
        while (aPosition > 0 && bPosition > 0) {
            int value = Integer.valueOf(numberA.substring(aPosition - 1, aPosition)) + Integer.valueOf(numberB.substring(bPosition - 1, bPosition));
            int finalValue = carryFlag ? value + 1 : value;
            if (value >= 10) {
                carryFlag = true;
            }
            else {
                carryFlag = false;
            }
            stringBuilder.append(finalValue % 10);
            aPosition--;
            bPosition--;
        }
        while (aPosition > 0 ) {
            int value = Integer.valueOf(numberA.substring(aPosition - 1, aPosition));
            int finalValue = carryFlag ? value + 1 : value;
            if (finalValue > 10) {
                carryFlag = true;
            }
            else {
                carryFlag = false;
            }
            stringBuilder.append(finalValue % 10);
            aPosition--;
        }

        while (bPosition > 0 ) {
            int value = Integer.valueOf(numberB.substring(bPosition - 1, bPosition));
            int finalValue = carryFlag ? value + 1 : value;
            if (finalValue > 10) {
                carryFlag = true;
            }
            else {
                carryFlag = false;
            }
            stringBuilder.append(finalValue % 10);
            bPosition--;
        }
        if (carryFlag) {
            stringBuilder.append(1);
        }
        return stringBuilder.reverse().toString();
    }

    public static void main(String[] args) {
        int[] arr = new int[10];
        for (int i = 0; i < 10; i++) {
            arr[i] = random.nextInt(10);
        }
        logger.info("sorted arr:", sortByBubble(arr));

        logger.info(add("998809999", "1232156789"));
    }
}