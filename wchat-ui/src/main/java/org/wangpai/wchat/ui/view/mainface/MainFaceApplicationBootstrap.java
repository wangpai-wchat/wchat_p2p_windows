package org.wangpai.wchat.ui.view.mainface;

import org.springframework.stereotype.Component;

/**
 * @since 2022-1-4
 */
//@DependsOn(value = {"wchatUiOnHooks", "wchatUiQueryHooks"})
@Component
public class MainFaceApplicationBootstrap {
    public void start(String[] args) {
        startUp(args);
    }

    public static void startUp(String[] args) {
        MainFaceApplication.main(args);
    }
}
