package org.wangpai.wchat.ui.view.base;

@FunctionalInterface
public interface UiAdjustable {
    /**
     * 进行一些需 UI 显示之后才能完成的工作
     *
     * 此方法需要在 UI 显示完成之后，手动调用
     *
     * @since 2021-11-7
     */
    void afterUiShow();
}
