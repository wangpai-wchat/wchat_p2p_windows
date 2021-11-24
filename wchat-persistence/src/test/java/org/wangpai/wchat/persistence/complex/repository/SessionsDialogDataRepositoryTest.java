package org.wangpai.wchat.persistence.complex.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wangpai.wchat.ui.model.external.driver.query.GetAllSessionsDialogDataRequest;
import org.wangpai.wchat.ui.model.external.driver.query.GetSessionDialogDataRequest;

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
public class SessionsDialogDataRepositoryTest {
    @Autowired
    SessionsDialogDataRepository repository;

    @Test
    public void getSessionDialogData() {
        var request = new GetSessionDialogDataRequest().setSessionId("104");
        var response = this.repository.getSessionDialogData(request);
        System.out.println(response);
    }

    @Test
    public void getAllSessionsDialogData() {
        var request = new GetAllSessionsDialogDataRequest().setBegin(0).setUnit(1);
        var response = this.repository.getAllSessionsDialogData(request);
        var dialogDataList = response.getDialogDataList();
        for (var dialogData : dialogDataList) {
            System.out.println(dialogData);
        }
    }
}