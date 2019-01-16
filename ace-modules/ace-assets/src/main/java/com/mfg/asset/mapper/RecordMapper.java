package com.mfg.asset.mapper;

import com.mfg.asset.entity.Record;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface RecordMapper extends Mapper<Record> {

    public List<Record> findAsset(Record record);

}
