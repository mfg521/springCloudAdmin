package com.mfg.asset.commom;


import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Create By mengfanguang on 2018/11/2 14:51
 * 通用mapper
 */

public interface CommonMapper<T> extends Mapper<T>,MySqlMapper<T> {
}
