package com.mfg.asset.mapper;

import com.mfg.asset.Vo.ShenChaVo;
import com.mfg.asset.entity.Asset;
import tk.mybatis.mapper.common.Mapper;

public interface AssetMapper extends Mapper<Asset> {
    public ShenChaVo getShenChaVo(String serialNumber);

}
