package org.wangpai.wchat.persistence.util;


import org.wangpai.wchat.persistence.origin.domain.User;

public class Converter {
    /**
     * 将数据库 User 转化为 UI User
     *
     * 因为本类位于数据库模块，所以这里选择对 UI User 使用全限定名
     *
     * @since 2022-1-16
     */
    public static org.wangpai.wchat.ui.model.functions.contacts.friends.User dbUser2UiUser(User dbUser) {
        return new org.wangpai.wchat.ui.model.functions.contacts.friends.User()
                .setId(dbUser.getId())
                .setIp(dbUser.getIp())
                .setPort(dbUser.getPort())
                .setNickname(dbUser.getNickname())
                .setAvatar(dbUser.getAvatar())
                .setRemarks(dbUser.getRemarks())
                .setMyContactCode(dbUser.getMyContactCode())
                .setOtherPartyContactCode(dbUser.getOtherPartyContactCode());
    }

    /**
     * 将 UI User 转化为数据库 User
     *
     * 因为本类位于数据库模块，所以这里选择对 UI User 使用全限定名
     *
     * @since 2022-1-16
     */
    public static User uiUser2DbUser(org.wangpai.wchat.ui.model.functions.contacts.friends.User uiUser) {
        return new User()
                .setId(uiUser.getId())
                .setIp(uiUser.getIp())
                .setPort(uiUser.getPort())
                .setNickname(uiUser.getNickname())
                .setAvatar(uiUser.getAvatar())
                .setRemarks(uiUser.getRemarks())
                .setMyContactCode(uiUser.getMyContactCode())
                .setOtherPartyContactCode(uiUser.getOtherPartyContactCode());
    }
}
