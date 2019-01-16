package com.mfg.asset.biz;

import com.mfg.asset.commom.ParameterNotValidException;
import com.mfg.asset.entity.Asset;
import com.mfg.asset.entity.Record;
import com.mfg.asset.mapper.AssetMapper;
import com.mfg.asset.mapper.RecordMapper;
import com.mfg.auth.common.biz.BaseBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class RecordBiz extends BaseBiz<RecordMapper, Record> {
    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private AssetMapper assetMapper;

    public List<Record> findAsset(Record record) {
        return recordMapper.findAsset(record);
    }

    @Transactional
    public Integer returnAsset(Record record) {
        //设置记录表归还时间和已经归还状态
        Integer updateRecordStatus = updateRecordStatus(record.getEmployeeUuid(), record.getSerialNumber(), null);

        //设置Asset表为可使用状态
        Integer returnAssets = updateAssetStatus(record.getSerialNumber(), 0);
        if (updateRecordStatus == 1 && returnAssets == 1) {
            return 1;
        } else {
            throw new ParameterNotValidException("归还失败，请稍后重试");
        }

    }

    @Transactional
    public Integer changeOrSaveAsset(Map recordVo) {

        Example assetExample = new Example(Asset.class);
        assetExample.createCriteria().andEqualTo("serialNumber", recordVo.get("serialNumber").toString());
        System.out.println(assetExample.toString());
        List<Asset> assets = assetMapper.selectByExample(assetExample);
        if (assets.size() == 0) {
            throw new ParameterNotValidException("No this Asset");
        }
        if (assets.get(0).getAssetStatus() != 0) {
            throw new ParameterNotValidException("The equipment has been occupied or damaged");
        }

        if (!assets.get(0).getAssetType().equals(recordVo.get("assetType"))) {
            String assetType = assets.get(0).getAssetType();
            throw new ParameterNotValidException("The equipment is " + assetType + ",Please reselect");
        }


        boolean curentSerialNumber = recordVo.containsKey("curentSerialNumber");
        //根据是否有当前的curentSerialNumber来判断是新增还是更改
        if (!curentSerialNumber) {
            //新增
            //生成新的借用资产的记录
            Integer newRecord = createNewRecord(recordVo.get("employeeUuid").toString(),
                    recordVo.get("serialNumber").toString(),
                    recordVo.get("assetType").toString());

            //修改资产表的为1
            Integer returnAssets = updateAssetStatus(recordVo.get("serialNumber").toString(), 1);

            if (newRecord == 1 && returnAssets == 1) {
                return 1;
            } else {
                throw new ParameterNotValidException("新增资产失败，请稍后重试");
            }

        } else {
            //修改
            //修改资产借用记录表，将之前的设备设置为空置状态，填写归还日期
            Integer returnRecord = updateRecordStatus(recordVo.get("employeeUuid").toString(),
                    recordVo.get("curentSerialNumber").toString(),
                    recordVo.get("assetType").toString());

            //生成新的借用资产的记录
            Integer newRecord = createNewRecord(recordVo.get("employeeUuid").toString(),
                    recordVo.get("serialNumber").toString(),
                    recordVo.get("assetType").toString());

            //修改资产表的为1
            Integer returnAssets = updateAssetStatus(recordVo.get("serialNumber").toString(), 1);

            //修改当前资产为0
            Integer borrowAssets = updateAssetStatus(recordVo.get("curentSerialNumber").toString(), 0);
            if (returnRecord == 1 && newRecord == 1 && returnAssets == 1 && borrowAssets == 1) {
                return 1;
            } else {
                throw new ParameterNotValidException("设置失败");
            }
        }


    }

    //设置Asset资产表状态
    public Integer updateAssetStatus(String serialNumber, Integer status) {
        Example assetExample = new Example(Asset.class);
        assetExample.createCriteria().andEqualTo("serialNumber", serialNumber);
        Asset asset = new Asset();
        asset.setAssetStatus(status);
        Integer updateStatus = assetMapper.updateByExampleSelective(asset, assetExample);
        return updateStatus;
    }

    //Record表修改状态为归还
    public Integer updateRecordStatus(String employeeUuid, String serialNumber, String assetType) {
        LocalDateTime localDateTime = LocalDateTime.now();
        Example returnExample = new Example(Record.class);
        returnExample.createCriteria().andEqualTo("employeeUuid", employeeUuid)
                .andEqualTo("assetType", assetType)
                .andEqualTo("serialNumber", serialNumber)
                .andEqualTo("isReturned", 0);
        Record record = new Record();
        record.setReturnDate(localDateTime);
        record.setIsReturned(1);
        int result = recordMapper.updateByExampleSelective(record, returnExample);
        return result;
    }

    //Record表生成新的资产借用记录表
    public Integer createNewRecord(String employeeUuid, String serialNumber, String assetType) {
        LocalDateTime borrowNewAsset = LocalDateTime.now();
        Record borrowNewrecord = new Record();
        borrowNewrecord.setEmployeeUuid(employeeUuid);
        borrowNewrecord.setSerialNumber(serialNumber);
        borrowNewrecord.setBorrowedDate(borrowNewAsset);
        borrowNewrecord.setAssetType(assetType);
        borrowNewrecord.setIsReturned(0);
        int insert = recordMapper.insertSelective(borrowNewrecord);
        return insert;
    }


}
