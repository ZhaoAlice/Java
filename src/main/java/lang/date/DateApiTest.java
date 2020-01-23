package lang.date;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.Period;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 〈learn java8 date api〉<br>
 *
 * @author Carrie
 * @create 2020/1/23
 * @since 1.0.0
 */
public class DateApiTest {

    public static void main(String[] args) {
        // localDate只显示到日期-年 月 日
        // 获取今天的日期
        LocalDate today = LocalDate.now();
        // today is 2020-01-23
        System.out.println("today is " + today);

        int year = today.getYear();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();

        System.out.println("year: " + year);
        System.out.println("montn: " + month);
        System.out.println("day: " + day);

        // 自定义日期
        LocalDate localDate1 = LocalDate.of(2020, 1, 23);
        System.out.println("custom date is: " + localDate1);

        System.out.println(localDate1.equals(today));

        MonthDay birthday = MonthDay.of(localDate1.getMonth(), localDate1.getDayOfMonth());
        MonthDay nowMonthday = MonthDay.from(today);
        // equals 方法的写法
        System.out.println(nowMonthday.equals(birthday));

        // localTime
        // 到毫秒 current time is: 09:19:41.677
        LocalTime time = LocalTime.now();
        System.out.println("current time is: " + time);

        // 加上五个小时后,会新创建一个对象,不会改变原来的时间
        LocalTime newTime = time.plusHours(5);
        System.out.println("new time is: " + newTime);

        // ChronoUnit
        LocalDate localDateByWeek = today.plus(1, ChronoUnit.WEEKS);
        System.out.println("The time added a week is: " + localDateByWeek);

        // 计算一年前或者一年后的日期
        LocalDate localDateByYear = today.minus(1, ChronoUnit.YEARS);
        System.out.println("The time added a year is: " + localDateByYear);

        Clock clock = Clock.systemUTC();
        // 同System.currentTimeMillis();
        System.out.println("clock time is: " + clock.millis());

        Clock defaultClockZone = Clock.systemDefaultZone();
        System.out.println("clock time is: " + defaultClockZone.millis());

        // 两个日期之间的比较
        System.out.println(today.isAfter(localDate1));
        System.out.println(today.isBefore(localDate1));

        // java中处理时区
        ZoneId america = ZoneId.of("America/New_York");
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime dateWithZone = ZonedDateTime.of(localDateTime, america);
        System.out.println("current time with zone is: " + dateWithZone.toLocalDate());

        // year month的使用
        YearMonth currentYearMonth = YearMonth.now();
        System.out.printf("Days in month year %s: %d%n", currentYearMonth, currentYearMonth.lengthOfMonth());

        // %s输出字符串 %n换行
        YearMonth creditExpiredDate = YearMonth.of(2021, Month.AUGUST);
        System.out.printf("The expired date of your credit is: %s %n", creditExpiredDate);

        // 能被4整除 不能被100整除 能被400整除
        System.out.println("this year is leap year:" + today.isLeapYear());

        // 计算两个时间的年 月 日 周时间差
        LocalDate localDate2 = LocalDate.of(2019, 10, 25);
        Period periodTwoTime = Period.between(today, localDate2);
        System.out.printf("the month day year number is: %d %d %d %n",
                periodTwoTime.getMonths(), periodTwoTime.getDays(), periodTwoTime.getYears());

        // 获取时间戳 同date
        Instant timestamp = Instant.now();
        System.out.println("timestamp is: " + timestamp.toEpochMilli());
    }
}