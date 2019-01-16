package com.mfg.asset.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Table(name = "room")
@Data
public class Room implements Serializable {

    @Id
    private Integer roomId;
    private Integer floor;
    private Integer roomNum;


    private List<Block> block;

}
