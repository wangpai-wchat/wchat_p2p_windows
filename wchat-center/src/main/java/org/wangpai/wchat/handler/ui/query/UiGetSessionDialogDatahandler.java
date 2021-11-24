package org.wangpai.wchat.handler.ui.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wangpai.wchat.persistence.complex.repository.SessionsDialogDataRepository;
import org.wangpai.wchat.ui.model.external.driver.query.GetSessionDialogDataRequest;
import org.wangpai.wchat.universal.driver.WchatRequest;
import org.wangpai.wchat.universal.driver.WchatResponse;
import org.wangpai.wchat.universal.handler.QueryHandler;

@Service
public class UiGetSessionDialogDatahandler implements QueryHandler {
    @Autowired
    SessionsDialogDataRepository repository;

    @Override
    public WchatResponse query(WchatRequest request) {
        return this.repository.getSessionDialogData((GetSessionDialogDataRequest) request);
    }
}
