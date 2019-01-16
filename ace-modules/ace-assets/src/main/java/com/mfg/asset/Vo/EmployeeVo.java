package com.mfg.asset.Vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeeVo implements Serializable {

    private  String currentEmployeeNum;

    private String employeeNum;

    private  Integer roomNum;

    private String xindex;

    private String yindex;

    private String floor;

    private Integer blockId;
}
