package org.wangpai.wchat.ui.model.functions.chats;

/**
 * @deprecated 2022-1-10 现在，本项目只实现 P2P模式，所以不再需要提供本类了
 * @since 2021-12-24
 */
@Deprecated
public enum CommunicationMode {
    P2P(), // C-C 模式
    MAIL(), // C-S-S 模式
    NOTEPAD() // C-S 模式
}
