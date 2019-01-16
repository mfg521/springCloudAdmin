package com.mfg.test;

import com.mfg.asset.entity.Room;
import com.mfg.asset.mapper.RoomMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class MybatisTest {

//    @Test
//    public void testConnection() throws IOException {
//        String resource = "mybatis-config.xml";
//        InputStream inputStream = Resources.getResourceAsStream(resource);
//        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
//        SqlSession openSession = sqlSessionFactory.openSession();
//        try {
//            RoomMapper mapper = openSession.getMapper(RoomMapper.class);
//            Room room = new Room();
//            room.setFloor(6);
//            room.setRoomNum(621);
//            System.out.println(mapper.getRoomBlockByRoomNum(room));
//        } finally {
//            openSession.close();
//        }
//
//    }
}
