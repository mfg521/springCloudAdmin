package com.mfg.asset.biz;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfg.asset.Vo.RoomInfoVo;
import com.mfg.asset.entity.Room;
import com.mfg.asset.mapper.RoomMapper;
import com.mfg.auth.common.biz.BaseBiz;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class RoomBiz extends BaseBiz<RoomMapper, Room> {


    @Autowired
    private RoomMapper roomMapper;

//    public RoomInfoVo roomInfo(String roomNum) throws JSONException, IOException {
//        Example roomExample = new Example(Room.class);
//        roomExample.createCriteria().andEqualTo("roomNum", roomNum);
//
//        Room room = roomMapper.selectOneByExample(roomExample);
//        String roomNormalComment = room.getRoomNormalComment();
//        String roomSpecialComment = room.getRoomSpecialComment();
//
//        JSONArray jsonArray = null;
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, String> map = mapper.readValue(roomNormalComment, Map.class);
//        List<Map<String, String>> list = null;
//        if (null != roomSpecialComment) {
//            jsonArray = new JSONArray(roomSpecialComment);
//            list = new ArrayList<>();
//            for (int i = 0; i < jsonArray.length(); i++) {
//                Map<String, String> listmap = mapper.readValue(jsonArray.getJSONObject(i).toString(), Map.class);
//                list.add(listmap);
//            }
//        }
//
//
//        RoomInfoVo roomInfoVo = new RoomInfoVo();
//
//        roomInfoVo.setRoomNormalComment(map);
//        roomInfoVo.setRoomSpecialComment(list);
//        roomInfoVo.setTitalSize(room.getTotalSize());
//        roomInfoVo.setRoomNum(room.getRoomNum().toString());
//
//        return roomInfoVo;
//    }

}
