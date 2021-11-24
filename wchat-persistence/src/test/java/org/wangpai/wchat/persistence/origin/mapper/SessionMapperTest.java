package org.wangpai.wchat.persistence.origin.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * 此注解 @SpringBootTest 不能替代 @SpringBootApplication。
 * 虽然运行本类时，@SpringBootApplication 标记的类的 main 方法不会运行。
 * 但如果没有注解 @SpringBootApplication，Bean 的自动注入将不会进行
 *
 * 此测试类不需要每次都运行
 *
 * @since 2021-12-30
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SessionMapperTest {
    @Autowired
    private SessionMapper sessionMapper;

    @ParameterizedTest
    @ValueSource(strings = "1")
    public void search(String id) {
        var session = this.sessionMapper.search(id);
        System.out.println(session);
    }

    @Test
    public void save() {
//        String id = "1";
//        SessionMode mode = SessionMode.PAIR;
//        String contactsId = "1";
//        int msgReminderCounter = 99;
//        boolean disturb = false;
//        String myContactCode = "wchat";
//        String otherPartyContactCode = "go ahead";
//
//        var session = new Session()
//                .setId(id)
//                .setMode(mode)
//                .setUserId(contactsId)
//                .setMsgReminderCounter(msgReminderCounter)
//                .setDisturb(disturb)
//                .setMyContactCode(myContactCode)
//                .setOtherPartyContactCode(otherPartyContactCode);
//
//        try {
//            System.out.println("更新行数：" + this.sessionMapper.save(session));
//        } catch (Exception exception) {
//            System.out.println(exception);
//            throw exception;
//        }
    }

    /**
     * 当使用了注解 @ParameterizedTest 后，就无需使用注解 @Test
     *
     * @since 2021-12-30
     */
    @ParameterizedTest
    @ValueSource(strings = "1")
    public void updateById(String id) {
        var session = this.sessionMapper.search(id);
        var newId = "10000";
        session.setId(newId);
        this.sessionMapper.updateById(session, id);

        var newSession = this.sessionMapper.search(newId);
        System.out.println(newSession);
    }

    /**
     * 当使用了注解 @ParameterizedTest 后，就无需使用注解 @Test
     *
     * @since 2021-12-29
     */
    @ParameterizedTest
    @ValueSource(strings = "1")
    public void delete(String id) {
        System.out.println("更新行数：" + this.sessionMapper.delete(id));
        assertNull(this.sessionMapper.search(id));
    }
}