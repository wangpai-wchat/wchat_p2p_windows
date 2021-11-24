package org.wangpai.wchat.persistence.complex.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 此注解 @SpringBootTest 不能替代 @SpringBootApplication。
 * 虽然运行本类时，@SpringBootApplication 标记的类的 main 方法不会运行。
 * 但如果没有注解 @SpringBootApplication，Bean 的自动注入将不会进行
 *
 * 此测试类不需要每次都运行
 *
 * @since 2022-1-11
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class RegistryDaoTest {
    @Autowired
    private RegistryDao registryDao;

    @Test
    public void getMyId() {
        System.out.println(this.registryDao.getMyId());
    }

    @Test
    public void getMyServerPort() {
        System.out.println(this.registryDao.getMyServerPort());
    }

    @Test
    public void getMe() {
        System.out.println(this.registryDao.getMe());
    }
}