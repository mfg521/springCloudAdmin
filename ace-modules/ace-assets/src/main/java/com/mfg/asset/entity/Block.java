package com.mfg.asset.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.Id;
import javax.persistence.Table;

@Alias("block")
@Table(name = "block")
@Data
public class Block {

    @Id
    private Integer id;

    private String mtop;

    private String mleft;

    private String mwidth;

    private String mheight;

    private String intervalWidth;

    private String intervalHeight;

    private Integer lineNum;

    private Integer totalSize;

    private Integer roomNum;

    private String blockComment;

}
