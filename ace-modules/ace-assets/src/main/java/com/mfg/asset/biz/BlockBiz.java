package com.mfg.asset.biz;

import com.mfg.asset.entity.Block;
import com.mfg.asset.mapper.BlockMapper;
import com.mfg.auth.common.biz.BaseBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class BlockBiz extends BaseBiz<BlockMapper, Block> {


}
