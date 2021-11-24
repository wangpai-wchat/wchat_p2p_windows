package org.wangpai.wchat.persistence.origin.domain;

/**
 * @since 2022-1-8
 */
public enum RegistryNames {
    ME("ME", String.class), // 储存的是我的 userId 值

    MY_ID("MY_ID", String.class), // 储存的是我的 userId 值

    MY_PORT("MY_PORT", int.class), // 储存的是我的服务器端口号
    ;
    private String name;

    private Class<?> valueType;

    RegistryNames(String name, Class<?> valueType) {
        this.name = name;
        this.valueType = valueType;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static RegistryNames getEnum(String str) {
        var names = RegistryNames.values();
        for (var key : names) {
            if (key.name.equals(str)) {
                return key;
            }
        }

        return null;
    }
}
