package org.wangpai.wchat.handler.ui.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wangpai.wchat.persistence.complex.repository.UserRepository;
import org.wangpai.wchat.ui.model.external.driver.event.OnUpdateFriendInfoEvent;
import org.wangpai.wchat.universal.driver.WchatEvent;
import org.wangpai.wchat.universal.driver.WchatFeedback;
import org.wangpai.wchat.universal.handler.EventHandler;

/**
 * @since 2022-1-16
 */
@Service
public class UiOnUpdateFriendInfoHandler implements EventHandler {
    @Autowired
    private UserRepository userRepository;

    @Override
    public WchatFeedback handle(WchatEvent onUpdateFriendInfoEvent) {
        var event = (OnUpdateFriendInfoEvent) onUpdateFriendInfoEvent;
        this.userRepository.updateById(event.getUser(), event.getUserId());

        return null;
    }
}
