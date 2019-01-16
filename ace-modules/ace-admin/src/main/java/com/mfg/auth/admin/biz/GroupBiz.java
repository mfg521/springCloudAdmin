package com.mfg.auth.admin.biz;

import com.ace.cache.annotation.CacheClear;
import com.mfg.auth.admin.constant.AdminCommonConstant;
import com.mfg.auth.admin.entity.Group;
import com.mfg.auth.admin.entity.Menu;
import com.mfg.auth.admin.entity.ResourceAuthority;
import com.mfg.auth.admin.mapper.GroupMapper;
import com.mfg.auth.admin.mapper.MenuMapper;
import com.mfg.auth.admin.mapper.ResourceAuthorityMapper;
import com.mfg.auth.admin.mapper.UserMapper;
import com.mfg.auth.admin.vo.AuthorityMenuTree;
import com.mfg.auth.admin.vo.GroupUsers;
import com.mfg.auth.common.biz.BaseBiz;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author mengfanguang
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GroupBiz extends BaseBiz<GroupMapper, Group> {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private ResourceAuthorityMapper resourceAuthorityMapper;

    @Override
    public void insertSelective(Group entity) {
        if (AdminCommonConstant.ROOT == entity.getParentId()) {
            entity.setPath("/" + entity.getCode());
        } else {
            Group parent = this.selectById(entity.getParentId());
            entity.setPath(parent.getPath() + "/" + entity.getCode());
        }
        super.insertSelective(entity);
    }

    @Override
    public void updateById(Group entity) {
        if (AdminCommonConstant.ROOT == entity.getParentId()) {
            entity.setPath("/" + entity.getCode());
        } else {
            Group parent = this.selectById(entity.getParentId());
            entity.setPath(parent.getPath() + "/" + entity.getCode());
        }
        super.updateById(entity);
    }


    /**
     * 获取群组关联用户
     *
     * @param groupId
     * @return
     */
    public GroupUsers getGroupUsers(int groupId) {
        return new GroupUsers(userMapper.selectMemberByGroupId(groupId), userMapper.selectLeaderByGroupId(groupId));
    }

    /**
     * 变更群主和用户
     *
     * @param groupId 要变更的组id
     * @param members 组成员
     * @param leaders 组领导
     */
    @CacheClear(pre = "permission")
    public void modifyGroupUsers(int groupId, String members, String leaders) {
        mapper.deleteGroupLeadersById(groupId);
        mapper.deleteGroupMembersById(groupId);
        //插入members和leaders
        if (!StringUtils.isEmpty(members)) {
            String[] mem = members.split(",");
            for (String m : mem) {
                mapper.insertGroupMembersById(groupId, Integer.parseInt(m));
            }
        }

        if (!StringUtils.isEmpty(leaders)) {
            String[] mem = members.split(",");
            for (String m : mem) {
                mapper.insertGroupLeadersById(groupId, Integer.parseInt(m));
            }
        }

    }

    /**
     * 变更群组关联的菜单
     * @param groupId 要变更的群组ID
     * @param menus 要变更的菜单,这个只变更menu,不包括element
     */

    @CacheClear(keys = {"permission:menu", "permission:u"})
    public void modifyAuthorityMenu(int groupId, String[] menus) {
        resourceAuthorityMapper.deleteByAuthorityIdANdResourceType(groupId+"",AdminCommonConstant.RESOURCE_TYPE_MENU);
        List<Menu> menuList=menuMapper.selectAll();
        Map<String,String> map=new HashMap<String,String>();
        for(Menu menu:menuList){
            map.put(menu.getId().toString(),menu.getParentId().toString());
        }
        Set<String> relationMenus=new HashSet<String>();
        relationMenus.addAll(Arrays.asList(menus));
        ResourceAuthority resourceAuthority=null;
        for(String menuId:menus){
            findParentId(map,relationMenus,menuId);
        }

        for(String menuId:relationMenus){
            resourceAuthority=new ResourceAuthority(AdminCommonConstant.AUTHORITY_TYPE_GROUP,AdminCommonConstant.RESOURCE_TYPE_MENU);
            resourceAuthority.setAuthorityId(groupId+"");
            resourceAuthority.setResourceId(menuId);
            resourceAuthority.setParentId("-1");
            resourceAuthorityMapper.insertSelective(resourceAuthority);
        }
    }

    private void findParentId(Map<String,String> map,Set<String> relationMenus,String id){
        String parentId=map.get(id);
        if(String.valueOf(AdminCommonConstant.ROOT).equals(id)){
            return;
        }
        relationMenus.add(parentId);
        findParentId(map,relationMenus,parentId);
    }

    /**
     * 分配资源权限
     * @param groupId 要分配资源的组ID
     * @param elementId 要分配的资源ID
     */
    @CacheClear(keys={"permission:ele","permission:u"})
    public void modifyAuthorityElement(int groupId,int elementId){
        ResourceAuthority authority=new ResourceAuthority(AdminCommonConstant.AUTHORITY_TYPE_GROUP,AdminCommonConstant.RESOURCE_TYPE_BTN);
        authority.setAuthorityId(groupId+"");
        authority.setResourceId(elementId+"");
        authority.setParentId("-1");
        resourceAuthorityMapper.insertSelective(authority);
    }

    /**
     * 移除资源权限
     * @param groupId
     * @param menuId
     * @param elementId
     */
    @CacheClear(keys={"permission:ele","permission:u"})
    public void removeAuthorityElement(int groupId,int menuId,int elementId){
        ResourceAuthority authority=new ResourceAuthority();
        authority.setAuthorityId(groupId+"");
        authority.setResourceId(elementId+"");
        authority.setParentId("-1");
        resourceAuthorityMapper.delete(authority);
    }

    /**
     * 获取群组相关联的菜单
     * @param groupId 群组的Id ,资源类型是 AUTHORITY_TYPE_GROUP group
     * @return
     */
    public List<AuthorityMenuTree> getAuthorityMenu(int groupId){
        List<Menu> menus=menuMapper.selectMenuByAuthorityId(String.valueOf(groupId),AdminCommonConstant.AUTHORITY_TYPE_GROUP);
        List<AuthorityMenuTree> trees=new ArrayList<AuthorityMenuTree>();
        AuthorityMenuTree node=null;
        for(Menu menu:menus){
            node=new AuthorityMenuTree();
            node.setText(menu.getTitle());
            BeanUtils.copyProperties(menu,node);
            trees.add(node);
        }
        return trees;
    }

    /**
     * 获取群组相关联的资源 对应是element
     * @param groupId
     * @return
     */
    public List<Integer> getAuthorityElement(int groupId){
        ResourceAuthority authority=new ResourceAuthority(AdminCommonConstant.AUTHORITY_TYPE_GROUP,AdminCommonConstant.RESOURCE_TYPE_BTN);
        authority.setAuthorityId(groupId+"");
        List<ResourceAuthority> authorities=resourceAuthorityMapper.select(authority);
        List<Integer> ids=new ArrayList<>();
        for(ResourceAuthority auth:authorities){
            ids.add(Integer.parseInt(auth.getResourceId()));
        }
        return ids;
    }


}
