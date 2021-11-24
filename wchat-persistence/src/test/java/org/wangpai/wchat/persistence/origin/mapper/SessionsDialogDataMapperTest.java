package org.wangpai.wchat.persistence.origin.mapper;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wangpai.wchat.persistence.origin.domain.SessionsDialogData;
import org.wangpai.wchat.universal.protocol.internal.ui.MsgType;
import org.wangpai.wchat.universal.util.time.OfflineTimeUtil;

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
public class SessionsDialogDataMapperTest {
    @Autowired
    private SessionsDialogDataMapper sessionsDialogDataMapper;

    @ParameterizedTest
    @ValueSource(strings = "1")
    public void search(String id) {
        var singleDialogData = this.sessionsDialogDataMapper.search(id);
        System.out.println(singleDialogData);
    }

    @Test
    public void save() {
        String id = "1";
        String sessionId = "1";
        String userId = "1";
        boolean isMe = false;
        LocalDateTime time = OfflineTimeUtil.getTime();
        MsgType msgType = MsgType.TEXT;
        String textMsg = "test";
        byte[] binaryMsg = null;

        var singleDialogData = new SessionsDialogData()
                .setId(id)
                .setSessionId(sessionId)
                .setUserId(userId)
//                .setMe(isMe)
                .setTime(time)
                .setMsgType(msgType)
                .setTextMsg(textMsg)
                .setBinaryMsg(binaryMsg);

        try {
            System.out.println("更新行数：" + this.sessionsDialogDataMapper.save(singleDialogData));
        } catch (Exception exception) {
            System.out.println(exception);
            throw exception;
        }
    }

    /**
     * 当使用了注解 @ParameterizedTest 后，就无需使用注解 @Test
     *
     * @since 2021-12-30
     */
    @ParameterizedTest
    @ValueSource(strings = "1")
    public void updateById(String id) {
        var singleDialogData = this.sessionsDialogDataMapper.search(id);
        var newId = "10000";
        singleDialogData.setId(newId);
        this.sessionsDialogDataMapper.updateById(singleDialogData, id);

        var newSession = this.sessionsDialogDataMapper.search(newId);
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
        System.out.println("更新行数：" + this.sessionsDialogDataMapper.delete(id));
        assertNull(this.sessionsDialogDataMapper.search(id));
    }

    public void beforeGetRecentDialogData() {
        for (int index = 101; index <= 200; ++index) {
            String id = index + "";
            String sessionId = "1";
            String userId = "1";
            boolean isMe = false;
            LocalDateTime time = OfflineTimeUtil.getTime();
            MsgType msgType = MsgType.TEXT;
            String textMsg = "test" + index;
            byte[] binaryMsg = null;

            var singleDialogData = new SessionsDialogData()
                    .setId(id)
                    .setSessionId(sessionId)
                    .setUserId(userId)
//                    .setMe(isMe)
                    .setTime(time)
                    .setMsgType(msgType)
                    .setTextMsg(textMsg)
                    .setBinaryMsg(binaryMsg);
            this.sessionsDialogDataMapper.save(singleDialogData);
        }
    }

    /**
     * 当使用了注解 @ParameterizedTest 后，就无需使用注解 @Test
     *
     * @since 2021-12-29
     */
    @Test
    public void getSessionDialogData() {
        try {
            this.beforeGetRecentDialogData();
        } catch (Exception exception) {
            // 如果方法 beforeGetRecentDialogData 抛出了异常，直接打印然后忽略
            System.out.println(exception);
        }

        int begin = 0;
        int unit = 10;
        var dialogData = this.sessionsDialogDataMapper.getAllSessionsDialogData(begin, unit);
        for (var data : dialogData) {
            System.out.println(data);
        }
    }
}