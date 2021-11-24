package org.wangpai.wchat.handler.ui.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wangpai.wchat.persistence.complex.dao.UserDao;
import org.wangpai.wchat.ui.model.external.driver.query.GetUserDetailsByUserIdRequest;
import org.wangpai.wchat.universal.driver.WchatRequest;
import org.wangpai.wchat.universal.driver.WchatResponse;
import org.wangpai.wchat.universal.handler.QueryHandler;

@Service
public class UiGetUserDetailsByUserIdHandler implements QueryHandler {
    @Autowired
    private UserDao userDao;

    @Override
    public WchatResponse query(WchatRequest wchatRequest) {
        var request = (GetUserDetailsByUserIdRequest) wchatRequest;
        return this.userDao.getUserDetailsByUserId(request.getUserId());
    }
}
