package org.wangpai.wchat.universal.util.time;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.wangpai.wchat.universal.util.time.OfflineTimeUtil;
import org.wangpai.wchat.universal.util.time.TimeMode;
import org.wangpai.wchat.universal.util.time.TimeUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimeUtilTest {

    public static void main(String[] args) throws IOException {
//        var date = new Date();
//        System.out.println(date);
//        System.out.println(TimeUtil.date2LocalDateTime(date));
//        System.out.println(LocalDateTime.now());
//
//        System.out.println(TimeUtil.isToday(LocalDateTime.of(LocalDate.of(2021, 11, 21), LocalTime.of(0, 0))));
//        var time1 = TimeUtil.formatLocalDateTime(TimeUtil.date2LocalDateTime(date).minusDays(1));
//        var time2 = TimeUtil.formatLocalDateTime(TimeUtil.date2LocalDateTime(date));
//        System.out.println(time1.length());
//        System.out.println(time2.length());

//        manualTest_getTime_Network();
        manualTest_getTime_Offline();
    }

    @Test
    public void test_date2LocalDateTime_void() {
        var date = new Date(0); // 初始时间将为 1970-01-01 08:00
        var localDateTime = TimeUtil.date2LocalDateTime(date);

        assertEquals(1970, localDateTime.getYear());
        assertEquals(1, localDateTime.getMonthValue());
        assertEquals(1, localDateTime.getDayOfMonth());
        assertEquals(8, localDateTime.getHour());
        assertEquals(0, localDateTime.getMinute());
    }

    @Test
    public void test_isToday() {
        assertTrue(TimeUtil.isToday(TimeUtil.getTime()));
    }

    public static void manualTest_getTime_Network() {
        TimeUtil.setTimeMode(TimeMode.NETWORK);
        System.out.println(TimeUtil.getTime());
        for (int time = 0; time < 100 - 2; ++time) {
            TimeUtil.getTime();
        }
        System.out.println(TimeUtil.getTime());
    }

    public static void manualTest_getTime_Offline() {
        TimeUtil.setTimeMode(TimeMode.OFFLINE);
        System.out.println(TimeUtil.getTime());
        for (int time = 0; time < 10000000 - 2; ++time) {
            TimeUtil.getTime();
        }
        System.out.println(TimeUtil.getTime());

    }

    @Test
    public void test_parse() {
        var now = OfflineTimeUtil.getTime();
        var parseResult = LocalDateTime.parse(now.toString());
        assertEquals(parseResult, now);
    }

    @Test
    public void test_localdatetime2MySQLDatetimeString() {
        var now = OfflineTimeUtil.getTime();
        System.out.println(now);

        System.out.println(TimeUtil.localdatetime2MySQLDatetimeString(now));

    }
}