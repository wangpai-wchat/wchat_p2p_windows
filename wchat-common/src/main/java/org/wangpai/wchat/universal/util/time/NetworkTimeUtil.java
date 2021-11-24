package org.wangpai.wchat.universal.util.time;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

/**
 * 此类用于联网获取时间
 *
 * @since 2021-11-21
 */
public class NetworkTimeUtil {
    // 此字段源自方法 getNetworkDate，只会精确到秒
    private static LocalDateTime lastTime = TimeUtil.date2LocalDateTime(new Date(0));

    /**
     * 此字段是为了解决获得的原始时间只能精确到秒，然后为了防止可能的连续再次获取的时间相同而引入的纳秒偏移量。
     * 此纳秒偏移量是虚拟值，只能确保在同一秒内此值递增
     *
     * @since 2021-11-21
     */
    private static int nanoOffset;

    public static LocalDateTime getTime() throws IOException {
        var now = TimeUtil.date2LocalDateTime(NetworkTimeUtil.getNetworkDate());
        if (now.equals(NetworkTimeUtil.lastTime)) {
            return now.plusNanos(NetworkTimeUtil.generateNanoOffset()); // 此方法不会更改 now 的值
        } else {
            NetworkTimeUtil.lastTime = now;
            NetworkTimeUtil.clearNanoOffset();
            return now;
        }
    }

    /**
     * 注意：这个时间只能精确到秒
     *
     * @throws IOException 如果不能联网，将会抛出此异常
     * @since 2021-11-21
     */
    private static Date getNetworkDate() throws IOException {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        URLConnection uc;
        uc = new URL("http://www.baidu.com/").openConnection();
        uc.connect();
        return new Date(uc.getDate());
    }

    private static int generateNanoOffset() {
        return ++NetworkTimeUtil.nanoOffset;
    }

    private static void clearNanoOffset() {
        NetworkTimeUtil.nanoOffset = 0;
    }

}
