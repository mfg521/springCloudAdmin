package com.mfg.auth.admin.biz;


import com.ace.cache.annotation.Cache;
import com.ace.cache.annotation.CacheClear;
import com.mfg.auth.admin.constant.AdminCommonConstant;
import com.mfg.auth.admin.entity.Menu;
import com.mfg.auth.admin.mapper.MenuMapper;
import com.mfg.auth.common.biz.BaseBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mengfanguang
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MenuBiz extends BaseBiz<MenuMapper,Menu>{

    /**
     * 查询到所有的权限菜单
     * @return
     */
    @Override
    @Cache(key="permission:menu")
    public List<Menu> selectListAll(){
        return super.selectListAll();
    }


    /**
     * 插入一个新的菜单
     * @param entity
     */
    @Override
    @CacheClear(keys={"permission:menu","permission"})
    public void insertSelective(Menu entity){

        //设置菜单的资源路径
        if(AdminCommonConstant.ROOT==entity.getParentId()){
            entity.setPath("/"+entity.getCode());
        }else{
            Menu parent=this.selectById(entity.getParentId());
            entity.setPath(parent.getPath()+"/"+entity.getCode());
        }
    }

    @Override
    @CacheClear(keys={"permission:menu","permission"})
    public void updateById(Menu entity) {
        if (AdminCommonConstant.ROOT == entity.getParentId()) {
            entity.setPath("/" + entity.getCode());
        } else {
            Menu parent = this.selectById(entity.getParentId());
            entity.setPath(parent.getPath() + "/" + entity.getCode());
        }
        super.updateById(entity);
    }

    @Override
    @CacheClear(keys={"permission:menu","permission"})
    public void updateSelectiveById(Menu entity) {
        super.updateSelectiveById(entity);
    }

    /**
     * 获取用户可以访问的菜单
     *
     * @param id
     * @return
     */
    @Cache(key = "permission:menu:u{1}")
    public List<Menu> getUserAuthorityMenuByUserId(int id) {
        return mapper.selectAuthorityMenuByUserId(id);
    }

    /**
     * 根据用户获取可以访问的系统
     *
     * @param id
     * @return
     */
    public List<Menu> getUserAuthoritySystemByUserId(int id) {
        return mapper.selectAuthoritySystemByUserId(id);
    }

}
