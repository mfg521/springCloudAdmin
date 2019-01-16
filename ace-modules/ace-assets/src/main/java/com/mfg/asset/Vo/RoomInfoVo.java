package com.mfg.asset.Vo;


import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class RoomInfoVo implements Serializable {

    private String roomNum;
    private Integer titalSize;
    private Map<String, String> roomNormalComment;
    private List<Map<String, String>> roomSpecialComment;
}
