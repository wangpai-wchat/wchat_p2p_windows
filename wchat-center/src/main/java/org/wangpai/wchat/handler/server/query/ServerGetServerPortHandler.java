package org.wangpai.wchat.handler.server.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wangpai.wchat.persistence.complex.dao.RegistryDao;
import org.wangpai.wchat.server.external.driver.query.GetServerResponse;
import org.wangpai.wchat.universal.driver.WchatRequest;
import org.wangpai.wchat.universal.driver.WchatResponse;
import org.wangpai.wchat.universal.handler.QueryHandler;

@Service
public class ServerGetServerPortHandler implements QueryHandler {
    @Autowired
    private RegistryDao registryDao;

    @Override
    public WchatResponse query(WchatRequest request) {
        return new GetServerResponse().setPort(this.registryDao.getMyServerPort());
    }
}
