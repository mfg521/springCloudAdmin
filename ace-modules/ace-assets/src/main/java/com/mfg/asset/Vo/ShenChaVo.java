package com.mfg.asset.Vo;

import lombok.Data;

import java.util.Date;

@Data
public class ShenChaVo {

    private String computerModel;
    private String assetType;
    private String serialNumber;
    private String taggerNumber;
    private String financeCode;
    private String beijingCode;
    private String employeeName;
    private Integer floor;
    private Integer roomNum;
    private Date borrowedDate;

}
