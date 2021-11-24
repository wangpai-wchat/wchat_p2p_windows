package org.wangpai.wchat.universal.util.id;

import org.junit.jupiter.api.Test;

public class IdUtilTest {

    @Test
    public void idGenerator() {
        System.out.println(IdUtil.idGenerator());
    }

    @Test
    public void idGenerator_String_String() {
        System.out.println(IdUtil.idGenerator("a", "b"));
        System.out.println(IdUtil.idGenerator(null, null));

    }

    @Test
    public void uuidGenerator() {
        System.out.println(IdUtil.uuidGenerator());
    }
}