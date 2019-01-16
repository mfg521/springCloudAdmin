package com.mfg.auth.admin.mapper;

import com.mfg.auth.admin.entity.ResourceAuthority;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface ResourceAuthorityMapper extends Mapper<ResourceAuthority> {
    public void deleteByAuthorityIdANdResourceType(@Param("authorityId")String authorityId,@Param("resourceType")String resourceType);
}
