package com.mfg.asset.biz;

import com.mfg.asset.Vo.EmployeeBlockVo;
import com.mfg.asset.Vo.EmployeeBlocksVo;
import com.mfg.asset.Vo.EmployeeVo;
import com.mfg.asset.commom.ParameterNotValidException;
import com.mfg.asset.entity.Block;
import com.mfg.asset.entity.Employee;
import com.mfg.asset.entity.Room;
import com.mfg.asset.mapper.BlockMapper;
import com.mfg.asset.mapper.EmployeeMapper;
import com.mfg.asset.mapper.RoomMapper;
import com.mfg.auth.common.biz.BaseBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class EmployeeBiz extends BaseBiz<EmployeeMapper, Employee> {

    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private BlockMapper blockMapper;

    public Integer changeEmpByDrop(Employee employee) {
        return employeeMapper.changeEmpByDrop(employee);
    }

    @Transactional
    public EmployeeBlocksVo findByRooms(Room room) {
        Room rooms = roomMapper.getRoomBlockByRoomNum(room);    //根据传过来的roomid查到具体的room。
        List<Block> blockList = rooms.getBlock();
        List<EmployeeBlockVo> emloyeeBlockVoList = new ArrayList<EmployeeBlockVo>();
        EmployeeBlocksVo employeeBlocksVo=new EmployeeBlocksVo();
        EmployeeBlockVo emloyeeBlockVo = null;
        //通过区块号找到所有相对应的的员工
        for (Block block : blockList) {
            emloyeeBlockVo = new EmployeeBlockVo();
            List<List<Employee>> employee = new ArrayList<>();  //二维员工
            List<Employee> empByBlockId = employeeMapper.getEmpByBlockId(block);
            Integer rows = block.getTotalSize() / block.getLineNum() + 1;   //行数
            Integer lines = block.getLineNum();                             //列数
            Integer endLineNum = block.getTotalSize() % block.getLineNum(); //最后一行的个数

            //将查询出来的empByBlockId补成和totalSize一样大小。
            for (int i = 1; i <= rows; i++) {                               //循环每一行
                for (int j = 1; j <= lines; j++) {                          //循环每一列
                    int countIndex = (i * lines) - (lines - j) - 1;         //当前的countIndex
                    if (countIndex < empByBlockId.size() && countIndex <= block.getTotalSize()) {                    //这个肯定一直满足
                        Integer rowNums = empByBlockId.get(countIndex).getXindex() + 1;
                        Integer lineNums = empByBlockId.get(countIndex).getYindex() + 1;
                        if (rowNums != i || lineNums != j) {
                            empByBlockId.add(countIndex, new Employee());
                        }
                    } else {//此时已经将employee遍历完，但是还有房间
                        empByBlockId.add(countIndex, new Employee());
                    }
                }
            }
            for (int i = 0; i < rows; i++) {
                if (i != (rows - 1)) {
                    List<Employee> employeees2 = empByBlockId.subList(i * lines, (i + 1) * lines);
                    employee.add(i, employeees2);
                } else {
                    List<Employee> employeees2 = empByBlockId.subList(i * lines, block.getTotalSize());
                    employee.add(i, employeees2);
                }
            }
            emloyeeBlockVo.setBlock(block);
            emloyeeBlockVo.setEmployee(employee);
            emloyeeBlockVoList.add(emloyeeBlockVo);
        }
        employeeBlocksVo.setEmployeeBlockVoList(emloyeeBlockVoList);
        employeeBlocksVo.setRoomNum(room.getRoomNum());
        return employeeBlocksVo;
    }

    @Transactional
    public Integer changeOrSaveEmpBlock(EmployeeVo employeeVo) {
        //通过roomNum 这个后面要改成唯一的。获取floor
        Integer blockId = employeeVo.getBlockId();
        Block block=new Block();
        block.setId(blockId);
        Example blockExample = new Example(Block.class);
        Block Findblock = blockMapper.selectByPrimaryKey(block);

        String currentEmployeeNum = employeeVo.getCurrentEmployeeNum();

        //说明是新增
        if (null == currentEmployeeNum || "".equals(currentEmployeeNum)) {
            int result = changeEmployeeLocationBlock(employeeVo.getEmployeeNum(), Integer.valueOf(employeeVo.getXindex()), Integer.valueOf(employeeVo.getYindex()),blockId);
            return result;
        } else {
            //说明是修改。先修改当前的employee座位信息为空，然后将对应的人的座位信息填写到当前座位
            Integer setCurrentEmpresult = changeEmployeeLocationBlock(currentEmployeeNum, -1, -1,blockId);
            Integer setEmpResult = changeEmployeeLocationBlock(employeeVo.getEmployeeNum(), Integer.valueOf(employeeVo.getXindex()), Integer.valueOf(employeeVo.getYindex()),blockId);
            if (setCurrentEmpresult == 1 && setEmpResult == 1) {
                return 1;
            } else {
                throw new ParameterNotValidException("系统错误，请联系IT人员");
            }

        }
    }

    //修改位置信息
    public Integer changeEmployeeLocationBlock(String employeeUuid,Integer xindex, Integer yindex,Integer blockId) {
        Example employeeExample = new Example(Employee.class);
        employeeExample.createCriteria().andEqualTo("employeeUuid", employeeUuid);
        Employee employee = new Employee();
        employee.setXindex(xindex);
        employee.setYindex(yindex);
        employee.setBlockId(blockId);
        int result = employeeMapper.updateByExampleSelective(employee, employeeExample);
        if (result > 0) {
            return result;
        } else {
            throw new ParameterNotValidException("修改座位信息失败，请稍后重试");
        }
    }


}
