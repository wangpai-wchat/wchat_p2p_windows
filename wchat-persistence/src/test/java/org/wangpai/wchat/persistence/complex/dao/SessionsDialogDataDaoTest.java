package org.wangpai.wchat.persistence.complex.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wangpai.wchat.ui.model.external.driver.query.GetSessionDialogDataRequest;

/**
 * 此注解 @SpringBootTest 不能替代 @SpringBootApplication。
 * 虽然运行本类时，@SpringBootApplication 标记的类的 main 方法不会运行。
 * 但如果没有注解 @SpringBootApplication，Bean 的自动注入将不会进行
 *
 * 此测试类不需要每次都运行
 *
 * @since 2022-1-9
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SessionsDialogDataDaoTest {
    @Autowired
    private SessionsDialogDataDao sessionsDialogDataDao;

//    @Test
//    public void getAllSessionsDialogData() {
//        var request = new GetAllSessionsDialogDataRequest();
//        var dialogDataList = this.sessionsDialogDataDao.getAllSessionsDialogData(request);
//        for (var dialogData : dialogDataList) {
//            System.out.println(dialogData);
//        }
//    }

    @Test
    public void getSessionDialogData() {
        var request = new GetSessionDialogDataRequest()
                .setSessionId("102");
        var dialogDataList = this.sessionsDialogDataDao.getSessionDialogData(request);
        for (var dialogData : dialogDataList) {
            System.out.println(dialogData);
        }
    }
}