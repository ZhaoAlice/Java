package lang.java14;

import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.junit.Test;

/**
 * 〈〉<br>
 *
 * @author Carrie
 * @create 2022/9/13
 * @since 1.0.0
 */
public class TestFeature {

    @Test
    public void testNullPoint() {
        String str = null;
        // java.lang.NullPointerException: Cannot invoke "String.length()" because "str" is null
        //System.out.println(str.length());
        Calendar c1 = Calendar.getInstance();
        c1.set(2022, 9 - 1, 29, 19, 12, 12);
        DateTime endDateTime = new DateTime(new Date(System.currentTimeMillis()));
        DateTime beginDateTime = new DateTime(c1.getTime());
        int ret = Days.daysBetween(beginDateTime, endDateTime).getDays();
        System.out.println(ret);
    }

    @Test
    public void testTextBlock() {
        String str = "text block \n" +
            "test block1";
        System.out.println(str);
        System.out.println(str.length());
        // \s添加空格  \取消换行
        String strBlock = """
            text block\s\
            test block1""";
        System.out.println(strBlock);
        System.out.println(strBlock.length());
    }
}