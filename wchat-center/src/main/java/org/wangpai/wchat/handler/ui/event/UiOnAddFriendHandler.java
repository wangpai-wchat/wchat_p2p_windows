package org.wangpai.wchat.handler.ui.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wangpai.wchat.persistence.complex.repository.UserRepository;
import org.wangpai.wchat.ui.model.external.driver.event.OnAddFriendEvent;
import org.wangpai.wchat.universal.driver.WchatEvent;
import org.wangpai.wchat.universal.driver.WchatFeedback;
import org.wangpai.wchat.universal.handler.EventHandler;

/**
 * @since 2022-1-16
 */
@Service
public class UiOnAddFriendHandler implements EventHandler {
    @Autowired
    UserRepository userRepository;

    @Override
    public WchatFeedback handle(WchatEvent onAddFriendEvent) {
        var event = (OnAddFriendEvent) onAddFriendEvent;
        this.userRepository.save(event.getUser());

        return null;
    }
}
