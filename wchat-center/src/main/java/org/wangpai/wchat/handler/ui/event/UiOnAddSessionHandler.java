package org.wangpai.wchat.handler.ui.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wangpai.wchat.persistence.origin.domain.Session;
import org.wangpai.wchat.persistence.origin.mapper.SessionMapper;
import org.wangpai.wchat.ui.model.external.driver.event.OnAddSessionEvent;
import org.wangpai.wchat.universal.driver.WchatEvent;
import org.wangpai.wchat.universal.driver.WchatFeedback;
import org.wangpai.wchat.universal.handler.EventHandler;

@Service
public class UiOnAddSessionHandler implements EventHandler {
    @Autowired
    SessionMapper sessionMapper;

    @Override
    public WchatFeedback handle(WchatEvent onAddSessionEvent) {
        var event = (OnAddSessionEvent) onAddSessionEvent;
        var session = new Session()
                .setId(event.getSessionId())
                .setMode(event.getMode())
                .setUserId(event.getUserId())
                .setDisturb(event.isDisturb())
                .setMsgReminderCounter(event.getMsgReminderCounter());
        this.sessionMapper.save(session);

        return null;
    }
}
