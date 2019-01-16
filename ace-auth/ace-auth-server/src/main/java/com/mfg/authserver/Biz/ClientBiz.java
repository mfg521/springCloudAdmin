package com.mfg.authserver.Biz;

import com.mfg.auth.common.biz.BaseBiz;
import com.mfg.authserver.entity.Client;
import com.mfg.authserver.entity.ClientService;
import com.mfg.authserver.mapper.ClientMapper;
import com.mfg.authserver.mapper.ClientServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ClientBiz extends BaseBiz<ClientMapper, Client> {

    @Autowired
    private ClientServiceMapper clientServiceMapper;
    @Autowired
    private ClientServiceBiz clientServiceBiz;

    public List<Client> getClientServices(int id) {
        return mapper.selectAuthorityServiceInfo(id);
    }

    //更改客户端的Service
    public void modifyClientServices(int id, String clients) {
        clientServiceMapper.deleteByServiceId(id);
        if (!StringUtils.isEmpty(clients)) {
            String[] mem = clients.split(",");
            for (String m : mem) {
                ClientService clientService = new ClientService();
                clientService.setServiceId(m);
                clientService.setClientId(id + "");
                clientServiceBiz.insertSelective(clientService);
            }
        }
    }

}
