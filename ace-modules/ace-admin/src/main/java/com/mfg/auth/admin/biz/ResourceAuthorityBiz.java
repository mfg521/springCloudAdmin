package com.mfg.auth.admin.biz;

import com.mfg.auth.admin.entity.ResourceAuthority;
import com.mfg.auth.admin.mapper.ResourceAuthorityMapper;
import com.mfg.auth.common.biz.BaseBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mengfanguang
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class ResourceAuthorityBiz extends BaseBiz<ResourceAuthorityMapper,ResourceAuthority> {
}
