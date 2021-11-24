package org.wangpai.wchat.universal.util.id;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import lombok.Setter;

/**
 * 这个类不是线程安全的
 *
 * uuid 默认是 32 字节的小写字母与数字
 *
 * @since 2022-1-13
 */
public class IdUtil {
    /**
     * 是否需要校验生成的 id 会与以前的重复。默认不校验
     *
     * @since 2022-1-13
     */
    @Setter
    private static boolean needFast = true;

    @Setter
    private static List<String> library = new LinkedList<>();

    public static String idGenerator() {
        String uuid;
        if (needFast) {
            uuid = uuidGenerator();
        } else {
            do {
                uuid = uuidGenerator();
            } while (library.contains(uuid));
            library.add(uuid);
        }

        return uuid;
    }

    public static String idGenerator(String prefix, String suffix) {
        var prefixFix = prefix == null ? "" : prefix;
        var suffixFix = suffix == null ? "" : suffix;
        return prefixFix + idGenerator() + suffixFix;
    }

    public static String uuidGenerator() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
