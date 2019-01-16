package com.mfg.auth.admin.rest;

import com.mfg.auth.admin.biz.GroupBiz;
import com.mfg.auth.admin.biz.ResourceAuthorityBiz;
import com.mfg.auth.admin.constant.AdminCommonConstant;
import com.mfg.auth.admin.entity.Group;
import com.mfg.auth.admin.vo.AuthorityMenuTree;
import com.mfg.auth.admin.vo.GroupTree;
import com.mfg.auth.admin.vo.GroupUsers;
import com.mfg.auth.common.msg.ObjectRestResponse;
import com.mfg.auth.common.rest.BaseController;
import com.mfg.auth.common.util.TreeUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mengfanguang
 * @create 2018-8-8
 */
@RestController
@RequestMapping("group")
public class GroupController extends BaseController<GroupBiz,Group> {

    @Autowired
    private ResourceAuthorityBiz resourceAuthorityBiz; //资源权限

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public List<Group> list(String name,String groupType){
        if(StringUtils.isBlank(name)&&StringUtils.isBlank(groupType)){
            return new ArrayList<Group>();
        }
        Example example=new Example(Group.class);
        if(StringUtils.isNotBlank(name)){
            example.createCriteria().andLike("name","%"+name+"%");
        }
        if(StringUtils.isNotBlank(groupType)){
            example.createCriteria().andEqualTo("groupType",groupType);
        }

        return  baseBiz.selectByExample(example);
    }

    @RequestMapping(value="/{id}/user",method = RequestMethod.PUT)
    public ObjectRestResponse modifiyUsers(@PathVariable int id, String members, String leaders){
        baseBiz.modifyGroupUsers(id, members, leaders);
        return new ObjectRestResponse().rel(true);
    }


    /**
     * GroupUsers 群组成员 每一个群组成员都包括leaders和members,都可以是多个
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}/user",method = RequestMethod.GET)
    public ObjectRestResponse<GroupUsers> getUsers(@PathVariable int id){
        return new ObjectRestResponse<GroupUsers>().rel(true).data(baseBiz.getGroupUsers(id));
    }

    /**
     * 更改群组相关联的菜单
     * @param id groupId
     * @param menuTrees
     * @return
     */
    @RequestMapping(value = "/{id}/authority/menu",method = RequestMethod.POST)
    public ObjectRestResponse modifyMenuAuthority(@PathVariable  int id, String menuTrees){
        String [] menus=menuTrees.split(",");
        baseBiz.modifyAuthorityMenu(id,menus);
        return new ObjectRestResponse().rel(true);
    }

    /**
     * 获取群组相关联的菜单
     * @param id groupId
     * @return
     */
    @RequestMapping(value = "/{id}/authority/menu", method = RequestMethod.GET)
    public ObjectRestResponse<List<AuthorityMenuTree>> getMenuAuthority(@PathVariable  int id){
        return new ObjectRestResponse<>().data(baseBiz.getAuthorityMenu(id)).rel(true);
    }


    /**
     * 获取群组相对应的资源，即可以访问的element
     * @param id groupId
     * @param menuId 这个是菜单id 在这个接口中并没有使用
     * @param elementId 资源elementId
     *                  下面的两个接口分别是移除和get
     * @return
     */
    @RequestMapping(value = "/{id}/authority/element/add", method = RequestMethod.POST)
    public ObjectRestResponse addElementAuthority(@PathVariable  int id,int menuId, int elementId){
        baseBiz.modifyAuthorityElement(id,elementId);
        return new ObjectRestResponse().rel(true);
    }

    @RequestMapping(value = "/{id}/authority/element/remove", method = RequestMethod.POST)
    public ObjectRestResponse removeElementAuthority(@PathVariable int id,int menuId, int elementId){
        baseBiz.removeAuthorityElement(id,menuId,elementId);
        return new ObjectRestResponse().rel(true);
    }

    @RequestMapping(value = "/{id}/authority/element", method = RequestMethod.GET)
    public ObjectRestResponse<List<Integer>> getElementAuthority(@PathVariable  int id){
        return new ObjectRestResponse().data(baseBiz.getAuthorityElement(id)).rel(true);
    }


    @RequestMapping(value = "/tree",method = RequestMethod.GET)
    public List<GroupTree> tree(String name,String groupType){
        if(StringUtils.isBlank(name)&&StringUtils.isBlank(groupType)){
            return new ArrayList<GroupTree>();
        }
        Example example=new Example(Group.class);
        if(StringUtils.isNotBlank(name)){
            example.createCriteria().andLike("name","%"+name+"%");
        }

        if(StringUtils.isNotBlank(groupType)){
            example.createCriteria().andEqualTo("groupType",groupType);
        }
        return getTree(baseBiz.selectByExample(example),AdminCommonConstant.ROOT);

    }

    private List<GroupTree> getTree(List<Group> groups,int root){
        List<GroupTree> trees=new ArrayList<GroupTree>();
        GroupTree node=null;
        for(Group group: groups){
            node=new GroupTree();
            node.setLabel(group.getName());
            BeanUtils.copyProperties(group,node);
            trees.add(node);
        }
        return TreeUtil.bulid(trees,root);
    }

}
