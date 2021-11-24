package org.wangpai.wchat;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.wangpai.wchat.server.Server;
import org.wangpai.wchat.server.external.driver.query.GetServerResponse;
import org.wangpai.wchat.ui.view.mainface.MainFaceApplicationBootstrap;
import org.wangpai.wchat.universal.util.multithreading.Multithreading;

@Getter
@Accessors(chain = true)
@Configuration
public class Center {
    private final Center center = this;

    @Autowired
    private Server server;

    @Autowired
    private MainFaceApplicationBootstrap mainFace;

    /**
     * 此方法不会启动模块 Client，原因是模块 Client 将在模块 UI 中懒启动，且会有多个实例
     *
     * @since 2022-1-4
     */
    public void start(String[] args) {
        this.startServer();
        this.startUi(args);
    }

    /**
     * @since 2022-1-4
     */
    public void startServer() {
        var response = (GetServerResponse) this.server.getServerPort(null);
        this.server.setPort(response.getPort());
        Multithreading.execute(() -> this.server.start());
    }

    /**
     * 此方法不会新建新建线程来启动 UI，原因是 UI 有自己的线程了
     *
     * @since 2022-1-4
     */
    public void startUi(String[] args) {
        this.mainFace.start(args);
    }
}
