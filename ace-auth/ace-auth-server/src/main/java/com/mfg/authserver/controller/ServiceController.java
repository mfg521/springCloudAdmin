package com.mfg.authserver.controller;

import com.mfg.auth.common.msg.ObjectRestResponse;
import com.mfg.auth.common.rest.BaseController;
import com.mfg.authserver.Biz.ClientBiz;
import com.mfg.authserver.entity.Client;
import com.mfg.authserver.entity.ClientService;
import org.springframework.web.bind.annotation.*;

/**
 * @author mengfanguang
 * @create 2017/12/26.
 */
@RestController
@RequestMapping("service")
public class ServiceController extends BaseController<ClientBiz,Client> {

    @RequestMapping(value = "/{id}/client", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse modifyUsers(@PathVariable int id, String clients){
        baseBiz.modifyClientServices(id, clients);
        return new ObjectRestResponse().rel(true);
    }

    @RequestMapping(value = "/{id}/client", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<ClientService> getUsers(@PathVariable int id){
        return new ObjectRestResponse<ClientService>().rel(true).data(baseBiz.getClientServices(id));
    }
}
