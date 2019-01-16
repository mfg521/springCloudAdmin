package com.mfg.asset.mapper;

import com.mfg.asset.entity.Block;
import com.mfg.asset.entity.Employee;
import com.mfg.asset.entity.Room;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface EmployeeMapper extends Mapper<Employee> {

    public Integer changeEmpByDrop(Employee employee);

    public List<Employee> getEmpByBlockId(Block block);
}
