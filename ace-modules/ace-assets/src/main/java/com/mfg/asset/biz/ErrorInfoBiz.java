package com.mfg.asset.biz;

import com.mfg.asset.entity.ErrorInfo;
import com.mfg.asset.mapper.ErrorInfoMapper;
import com.mfg.auth.common.biz.BaseBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ErrorInfoBiz extends BaseBiz<ErrorInfoMapper, ErrorInfo> {

}
