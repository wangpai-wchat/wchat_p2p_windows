package org.wangpai.wchat.universal.util.time;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.wangpai.wchat.universal.util.Database;

@Accessors(chain = true)
public class TimeUtil {
    @Getter
    @Setter
    private static TimeMode timeMode = TimeMode.DEFAULT;

    /**
     * 表示上一次获取的时间是联网时间，还是本地离线时间。
     * 此字段只有在 timeMode 为 DEFAULT 时才有效
     *
     * @since 2021-11-21
     */
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private static TimeMode timeState;

    @Getter
    @Setter(AccessLevel.NONE) // 此字段禁止自动生成 setter 方法，因为已经有了同名的方法了
    private static boolean cached = false;

    private static LocalDateTime cachedTime;

    public static void setCached(boolean cached) {
        if (!cached) { // 关闭缓存时，清除缓存
            TimeUtil.cachedTime = null;
        }
        TimeUtil.cached = cached;
    }

    public static LocalDateTime date2LocalDateTime(Date date, ZoneId zoneId) {
        return date.toInstant().atZone(zoneId).toLocalDateTime();
    }

    /**
     * 此方法视时区为中国大陆时区
     *
     * @since 2021-11-21
     */
    public static LocalDateTime date2LocalDateTime(Date date) {
        return TimeUtil.date2LocalDateTime(date, ZoneId.of("Asia/Shanghai"));
    }

    /**
     * @deprecated 2021-12-30
     * @since 2021-12-30
     */
    @Deprecated
    public static String localdatetime2MySQLDatetimeString(LocalDateTime localdatetime) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localdatetime);
    }

    /**
     * @deprecated 2021-12-30
     * @since 2021-12-30
     */
    @Deprecated
    public static LocalDateTime mySQLDateTimeString2LocalDateTime(String mySQLDatetime) {
        return TimeUtil.parse(mySQLDatetime);
    }

    private static LocalDateTime getRealTime() {
        return switch (TimeUtil.timeMode) {
            case DEFAULT -> {
                try {
                    TimeUtil.setTimeState(TimeMode.NETWORK);
                    yield NetworkTimeUtil.getTime();
                } catch (IOException e) {
                    TimeUtil.setTimeState(TimeMode.OFFLINE);
                    yield OfflineTimeUtil.getTime();
                }
            }
            case NETWORK -> {
                try {
                    TimeUtil.setTimeState(TimeMode.DEFAULT);
                    yield NetworkTimeUtil.getTime();
                } catch (IOException e) {
                    TimeUtil.setTimeState(TimeMode.NETWORK_ERROR);
                    yield null;
                }
            }
            case OFFLINE -> {
                TimeUtil.setTimeState(TimeMode.DEFAULT);
                yield OfflineTimeUtil.getTime();
            }
            default -> null; // 此 case 不应该发生
        };
    }


    private static int getTimeCounter = 0;

    /**
     * 注意：
     * 1. 此方法将会影响 timeState 的值
     * 2. 如果在网络不通时， timeMode 又为 NETWORK，则此时此方法将返回 null，并将 timeState 设为 NETWORK_ERROR
     *
     * @since 2021-11-21
     */
    public static LocalDateTime getTime() {
        ++getTimeCounter;
        System.out.println(getTimeCounter + "#getTime called：" + (System.currentTimeMillis() - Database.START_TIME));
        var result = TimeUtil.getRealTime();
        System.out.println(getTimeCounter + "#getTime return：" + (System.currentTimeMillis() - Database.START_TIME));
        return result;
    }

    /**
     * 如果 time 是今天，显示为：时:分
     * 如果 time 不是今天，显示为：年/月/日
     *
     * @since 2021-11-21
     */
    public static String formatLocalDateTime(LocalDateTime time) {
        if (TimeUtil.isToday(time)) {
            return DateTimeFormatter.ofPattern("HH:mm").format(time);
        } else {
            return DateTimeFormatter.ofPattern("yyyy/MM/dd").format(time);
        }
    }

    private static int todayCounter = 0;
    private static int todayRealCounter = 0;

    /**
     * 注意：此方法将会影响 timeState 的值
     *
     * @since 2021-11-21
     */
    public static boolean isToday(LocalDateTime dateTime) {
        todayCounter++;
        System.out.println(todayCounter + "#isToday called：" + (System.currentTimeMillis() - Database.START_TIME));
        if (!TimeUtil.isCached() || TimeUtil.cachedTime == null) {
            ++todayRealCounter;
            System.out.println(todayRealCounter + "#isToday real called：" + (System.currentTimeMillis() - Database.START_TIME));
            var realTime = TimeUtil.getTime();
            TimeUtil.cachedTime = realTime;
            System.out.println(todayRealCounter + "#isToday real return：" + (System.currentTimeMillis() - Database.START_TIME));

            return TimeUtil.isTheSameDay(realTime, dateTime);
        } else {
            return TimeUtil.isTheSameDay(TimeUtil.cachedTime, dateTime);
        }

    }

    /**
     * @since 2021-11-21
     */
    public static boolean isTheSameDay(LocalDateTime first, LocalDateTime second) {
        var firstDay = first.toLocalDate();
        var secondDay = second.toLocalDate();
        return firstDay.equals(secondDay);
    }

    public static LocalDateTime parse(String timeString) {
        return LocalDateTime.parse(timeString);
    }

    /**
     * @since 2021-11-24
     */
    public static int generateTimeSeed() {
        var now = OfflineTimeUtil.getTime();
        return now.getNano();
    }
}
