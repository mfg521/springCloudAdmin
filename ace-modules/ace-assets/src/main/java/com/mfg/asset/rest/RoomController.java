package com.mfg.asset.rest;

import com.mfg.asset.Vo.RoomInfoVo;
import com.mfg.asset.biz.RoomBiz;
import com.mfg.asset.entity.Room;
import com.mfg.auth.common.rest.BaseController;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("room")
public class RoomController extends BaseController<RoomBiz,Room> {

    @Autowired
    private RoomBiz roomBiz;

//    @RequestMapping(value = "/roomInfo", method = RequestMethod.POST)
//    public RoomInfoVo roomInfo(@RequestBody String roomNum) throws JSONException, IOException {
//        System.out.println(roomNum);
//        return roomBiz.roomInfo(roomNum);
//    }
}
