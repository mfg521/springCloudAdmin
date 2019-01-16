package com.mfg.asset.Vo;

import lombok.Data;

import java.util.List;

@Data
public class EmployeeBlocksVo {
    private Integer roomNum;
    private List<EmployeeBlockVo> employeeBlockVoList;
}
