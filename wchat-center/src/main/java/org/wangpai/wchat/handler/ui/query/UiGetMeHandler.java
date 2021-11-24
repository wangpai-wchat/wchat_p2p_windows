package org.wangpai.wchat.handler.ui.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wangpai.wchat.persistence.complex.dao.RegistryDao;
import org.wangpai.wchat.ui.model.external.driver.query.GetMeResponse;
import org.wangpai.wchat.ui.model.functions.contacts.friends.User;
import org.wangpai.wchat.universal.driver.WchatRequest;
import org.wangpai.wchat.universal.driver.WchatResponse;
import org.wangpai.wchat.universal.handler.QueryHandler;

@Service
public class UiGetMeHandler implements QueryHandler {
    @Autowired
    RegistryDao registryDao;

    @Override
    public WchatResponse query(WchatRequest request) {
        var dbUser = this.registryDao.getMe();
        var uiUser = new User()
                .setId(dbUser.getId())
                .setIp(dbUser.getIp())
                .setPort(dbUser.getPort())
                .setNickname(dbUser.getNickname())
                .setAvatar(dbUser.getAvatar())
                .setRemarks(dbUser.getRemarks())
                .setMyContactCode(dbUser.getMyContactCode())
                .setOtherPartyContactCode(dbUser.getOtherPartyContactCode());
        return new GetMeResponse().setMe(uiUser);
    }
}
