package com.mfg.auth.admin.biz;

import com.mfg.auth.admin.entity.GroupType;
import com.mfg.auth.admin.mapper.GroupTypeMapper;
import com.mfg.auth.common.biz.BaseBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class GroupTypeBiz extends BaseBiz<GroupTypeMapper,GroupType> {
}
