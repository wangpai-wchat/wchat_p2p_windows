package org.wangpai.wchat.persistence.origin.mapper;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wangpai.wchat.persistence.origin.domain.User;

import static org.junit.jupiter.api.Assertions.assertNull;

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
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    /**
     * 当使用了注解 @ParameterizedTest后，就无需使用注解 @Test
     *
     * @since 2021-12-29
     */
    @ParameterizedTest
    @ValueSource(strings = "1")
    public void search(String id) {
        var contacts = this.userMapper.search(id);
        System.out.println(contacts);
        System.out.println(new String(contacts.getAvatar(), StandardCharsets.UTF_8));
    }

    @Test
    public void save() {
        String id = "1";
        String ip = "127.0.0.1";
        String port = "13306";
        String nickname = "test";
        byte[] avatar = "test_avatar".getBytes(StandardCharsets.UTF_8);
        String remarks = "quiz";

        var contact = new User()
                .setId(id)
                .setIp(ip)
                .setPort(port)
                .setNickname(nickname)
                .setAvatar(avatar)
                .setRemarks(remarks);

        try {
            System.out.println("更新行数：" + this.userMapper.save(contact));
        } catch (Exception exception) {
            System.out.println(exception);
            throw exception;
        }
    }

    /**
     * 当使用了注解 @ParameterizedTest 后，就无需使用注解 @Test
     *
     * @since 2021-12-29
     */
    @ParameterizedTest
    @ValueSource(strings = "1")
    public void updateById(String id) {
        var contacts = this.userMapper.search(id);
        var newId = "10000";
        contacts.setId(newId);
        this.userMapper.updateById(contacts, id);

        var newContact = this.userMapper.search(newId);
        System.out.println(newContact);
    }

    /**
     * 当使用了注解 @ParameterizedTest 后，就无需使用注解 @Test
     *
     * @since 2021-12-29
     */
    @ParameterizedTest
    @ValueSource(strings = "1")
    public void delete(String id) {
        System.out.println("更新行数：" + this.userMapper.delete(id));
        assertNull(this.userMapper.search(id));
    }
}