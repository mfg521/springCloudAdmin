package com.mfg.asset.Vo;

import com.mfg.asset.entity.Block;
import com.mfg.asset.entity.Employee;
import lombok.Data;

import java.util.List;

@Data
public class EmployeeBlockVo {

    private List<List<Employee>> employee;

    private Block block;
}
