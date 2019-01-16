package com.mfg.asset.mapper;

import com.mfg.asset.entity.Room;
import tk.mybatis.mapper.common.Mapper;

public interface RoomMapper extends Mapper<Room> {

    public Room findById(Room room);

    public Room getRoomBlockByRoomNum(Room room);

}
