package org.wangpai.wchat.persistence.origin.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wangpai.wchat.persistence.origin.domain.Registry;

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
public class RegistryMapperTest {
    @Autowired
    private RegistryMapper registryMapper;

    @Test
    public void getValue() {
        var registry = this.registryMapper.getValue("TEXT");
        System.out.println(registry);
        System.out.println(registry.getValue());
    }

    @Test
    public void save() {
        var registry = new Registry()
                .setName("TEXT")
                .setValue("2022年1月8日");

        try {
            System.out.println("更新行数：" + this.registryMapper.save(registry));
        } catch (Exception exception) {
            System.out.println(exception);
            throw exception;
        }
    }

    @Test
    public void updateByName() {
        var name = "TEXT";
        var registry = new Registry()
                .setName(name)
                .setValue("2022年1月8日？？？？？？");

        try {
            System.out.println("更新行数：" + this.registryMapper.updateByName(registry, name));
        } catch (Exception exception) {
            System.out.println(exception);
            throw exception;
        }
    }

    @Test
    public void deleteByName() {
        var name = "TEXT";

        try {
            System.out.println("更新行数：" + this.registryMapper.deleteByName(name));
        } catch (Exception exception) {
            System.out.println(exception);
            throw exception;
        }
    }
}