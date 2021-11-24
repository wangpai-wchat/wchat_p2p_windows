package org.wangpai.wchat.universal.util.time;

import org.wangpai.wchat.universal.util.time.OfflineTimeUtil;

public class OfflineTimeUtilTest {
    public static void main(String[] args) {
//        System.out.println(OfflineTimeUtil.getTime());
//        System.out.println(OfflineTimeUtil.getTime().toString().length());
//        var now = OfflineTimeUtil.getTime();
//        var parseResult = LocalDateTime.parse(now.toString());
        String time = OfflineTimeUtil.getTime().toString();

        System.out.println(time);
//        System.out.println(time.length());
//        System.out.println(now.isEqual(parseResult));


    }

}