package com.mfg.auth.admin.rest;

import com.mfg.auth.admin.biz.ElementBiz;
import com.mfg.auth.admin.biz.UserBiz;
import com.mfg.auth.admin.entity.Element;
import com.mfg.auth.common.msg.ObjectRestResponse;
import com.mfg.auth.common.msg.TableResultResponse;
import com.mfg.auth.common.rest.BaseController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.List;

/**
 * @author mengfanguang
 */
@RestController
@RequestMapping("element")
public class ElementController extends BaseController<ElementBiz,Element> {

    @Autowired
    private UserBiz userBiz;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public TableResultResponse<Element> page(@RequestParam(defaultValue = "10") int limit,
                                             @RequestParam(defaultValue = "1") int offset,String name, @RequestParam(defaultValue = "0") int menuId){
        Example example=new Example(Element.class);
        Example.Criteria criteria= example.createCriteria();
        criteria.andEqualTo("menuId",menuId);
        if(StringUtils.isNotBlank(name)){
            criteria.andLike("name","%"+name+"%");
        }
        List<Element> elementList=baseBiz.selectByExample(example);
        return new TableResultResponse<>(elementList.size(),elementList);
    }

    @RequestMapping(value = "/user",method =RequestMethod.GET)
    public ObjectRestResponse<Element>getAuthorityElement(String menuId){
        int userId=userBiz.getUserByUsername(getCurrentUserName()).getId();
        List<Element> elements=baseBiz.getAuthorityElementByUserId(userId+"",menuId);
        return new ObjectRestResponse<List<Element>>().data(elements);
    }

    @RequestMapping(value = "/user/menu",method = RequestMethod.GET)
    public ObjectRestResponse<Element> getAuthorityElement(){
        int userId=userBiz.getUserByUsername(getCurrentUserName()).getId();
        List<Element> elements=baseBiz.getAuthorityElementByUserId(userId+"");
        return new ObjectRestResponse<List<Element>>().data(elements);
    }

}
