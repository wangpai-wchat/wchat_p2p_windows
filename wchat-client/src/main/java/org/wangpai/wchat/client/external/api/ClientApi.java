package org.wangpai.wchat.client.external.api;

import java.time.LocalDateTime;
import org.wangpai.wchat.client.external.config.ClientManager;
import org.wangpai.wchat.universal.protocol.internal.client.ClientConfig;
import org.wangpai.wchat.universal.protocol.internal.server.ContactCode;
import org.wangpai.wchat.universal.protocol.internal.server.DataUnit;

public class ClientApi {
    public static void send(DataUnit dataUnit, ClientConfig config, ContactCode contactCode, LocalDateTime dataTime) {
        ClientManager.getClient(config).send(dataUnit, contactCode, dataTime);
    }
}
