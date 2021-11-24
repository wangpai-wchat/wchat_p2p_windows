package org.wangpai.wchat.persistence.complex.dao;

import org.junit.jupiter.api.Test;
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
 * @since 2022-1-16
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserDaoTest {
    @Autowired
    private UserDao userDao;

    @Test
    public void getUserDetailsByUserId() {
        System.out.println(this.userDao.getUserDetailsByUserId("1001"));
    }

    @Test
    public void getUserByClientConfig() {
        var clientConfig = new ClientConfig().setOtherPartyIp("127.0.0.1").setOtherPartyPort("13314");
        System.out.println(this.userDao.getUserByClientConfig(clientConfig));
    }
}