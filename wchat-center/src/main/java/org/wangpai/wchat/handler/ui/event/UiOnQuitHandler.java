package org.wangpai.wchat.handler.ui.event;

import org.springframework.stereotype.Service;
import org.wangpai.wchat.client.external.config.ClientManager;
import org.wangpai.wchat.universal.driver.WchatEvent;
import org.wangpai.wchat.universal.driver.WchatFeedback;
import org.wangpai.wchat.universal.handler.EventHandler;
import org.wangpai.wchat.universal.util.multithreading.CentralDatabase;

@Service
public class UiOnQuitHandler implements EventHandler {
    @Override
    public WchatFeedback handle(WchatEvent event) {
        CentralDatabase.multithreadingClosed();
        ClientManager.closeAllConnetions();
        
        System.out.println("----OnQuit-----");
        return null;
    }
}
