package org.wangpai.wchat.server.external.api;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wangpai.wchat.server.Server;
import org.wangpai.wchat.server.external.driver.query.GetServerResponse;

/**
 * @since 2022-1-9
 */
@Service
public class ServerApi implements InitializingBean {
    /**
     * 静态成员无法直接自动装载，必须依赖其它方法
     *
     * @since 2022-1-9
     */
    private static Server server;

    /**
     * 用于完成对静态字段 server 的辅助字段
     *
     * @since 2022-1-9
     */
    @Autowired
    private Server auxiliaryServer;

    public static String getServerPort() {
        var response = (GetServerResponse) server.getServerPort(null);

        return response.getPort();
    }

    @Override
    public void afterPropertiesSet() {
        ServerApi.server = this.auxiliaryServer;

    }
}
