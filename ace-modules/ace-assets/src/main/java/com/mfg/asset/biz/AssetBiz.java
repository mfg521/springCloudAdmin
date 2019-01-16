package com.mfg.asset.biz;

import com.mfg.asset.Vo.ShenChaVo;
import com.mfg.asset.entity.Asset;
import com.mfg.asset.entity.Record;
import com.mfg.asset.mapper.AssetMapper;
import com.mfg.asset.mapper.RecordMapper;
import com.mfg.auth.common.biz.BaseBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class AssetBiz extends BaseBiz<AssetMapper, Asset> {

    @Autowired
    private RecordMapper recordMapper;

    public ShenChaVo getShenChaInfo(String serialNumber) {
        Example recordExample = new Example(Record.class);
        recordExample.createCriteria().andEqualTo("serialNumber", serialNumber);
        recordExample.createCriteria().andEqualTo("isReturned",0);
        List<Record> recordList=recordMapper.selectByExample(recordExample);
        if(recordList.size()>0) {
            return mapper.getShenChaVo(serialNumber);
        }else {
            Example assetExample = new Example(Asset.class);
            assetExample.createCriteria().andEqualTo("serialNumber",serialNumber);
            Asset asset = mapper.selectOneByExample(assetExample);
            ShenChaVo shenChaVo=new ShenChaVo();
            shenChaVo.setAssetType(asset.getAssetType());
            shenChaVo.setBeijingCode(asset.getBeijingCode());
            shenChaVo.setComputerModel(asset.getComputerModel());
            shenChaVo.setFinanceCode(asset.getFinanceCode());
            shenChaVo.setTaggerNumber(asset.getTaggerNumber());
            shenChaVo.setSerialNumber(asset.getSerialNumber());
            shenChaVo.setEmployeeName("IT");
            return shenChaVo;
        }
    }
}
