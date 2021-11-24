package org.wangpai.wchat.client.external.config;

import java.util.HashMap;
import java.util.Map;
import org.wangpai.wchat.client.Client;
import org.wangpai.wchat.universal.protocol.internal.client.ClientConfig;

/**
 * @since 2022-1-2
 */
public class ClientManager {
    private static Map<ClientConfig, Client> clients = new HashMap<>();

    /**
     * 通过 config 来查找 Client。如果没有找到，则新建一个
     *
     * 这个方法不是线程的，仅限 Center 线程调用
     *
     * @since 2022-1-2
     */
    public static Client getClient(ClientConfig clientConfig) {
        var client = clients.get(clientConfig);
        if (client == null || client.isClosed()) {
            client = Client.getInstance()
                    .setClientConfig(clientConfig);
            clients.put(clientConfig, client);
        }
        return client;
    }

    public static int closeAllConnetions() {
        int num = 0;
        for (var client : clients.values()) {
            client.close();
            ++num;
        }
        return num;
    }
}
