package org.wangpai.wchat.universal.spring;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Accessors(chain = true)
@Configuration("springContext")
public class SpringContext implements ApplicationContextAware {
    @Getter
    private static ApplicationContext springContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContext.springContext = applicationContext;
    }
}
