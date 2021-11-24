package org.wangpai.wchat.persistence.complex.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wangpai.wchat.universal.protocol.internal.client.ClientConfig;

/**
 * 此注解 @SpringBootTest 不能替代 @SpringBootApplication。
 * 虽然运行本类时，@SpringBootApplication 标记的类的 main 方法不会运行。
 * 但如果没有注解 @SpringBootApplication，Bean 的自动注入将不会进行
 *
 * 此测试类不需要每次都运行
 *
 * @since 2021-12-29
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SessionDaoTest {
    @Autowired
    private SessionDao sessionDao;

    /**
     * 当使用了注解 @ParameterizedTest后，就无需使用注解 @Test
     *
     * @since 2022-1-5
     */
    @ParameterizedTest
    @ValueSource(strings = "10000")
    public void getClientConfig(String sessionId) {
        var clientConfig = this.sessionDao.getClientConfig(sessionId);
        System.out.println(clientConfig);
    }

    @Test
    public void getSessionId() {
        var clientConfig = new ClientConfig()
                .setOtherPartyIp("127.0.0.1")
                .setOtherPartyPort("13306");
        var sessionId = this.sessionDao.getSessionId(clientConfig);
        System.out.println(sessionId);
    }

    @Test
    public void getAllSessionsInfo() {
        var sessionInfoList = this.sessionDao.getAllSessionsInfo();
        for (var sessionInfo : sessionInfoList) {
            System.out.println(sessionInfo);
        }
        System.out.println("个数：" + sessionInfoList.size());
    }

    @ParameterizedTest
    @ValueSource(strings = "104")
    public void getSessionInfo(String sessionId) {
        System.out.println(this.sessionDao.getSessionInfo(sessionId));
    }
}