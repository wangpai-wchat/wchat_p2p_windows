package org.wangpai.wchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.wangpai.wchat.universal.spring.SpringContext;

/**
 * 在同一 Maven 模块中，注解 @SpringBootApplication 只能使用一次
 *
 * @since 2021-12-29
 */
@SpringBootApplication(scanBasePackages = {"org.wangpai.wchat"})
public class Entrance {
    public static void main(String[] args) {
        SpringApplication.run(Entrance.class, args);

        var springContext = SpringContext.getSpringContext();
        var center = springContext.getBean(Center.class);
        center.start(args);
    }
}
