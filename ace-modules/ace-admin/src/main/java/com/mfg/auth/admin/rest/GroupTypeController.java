package com.mfg.auth.admin.rest;

import com.mfg.auth.admin.biz.GroupTypeBiz;
import com.mfg.auth.admin.entity.GroupType;
import com.mfg.auth.common.rest.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @author mengfanguang
 * @create 2018-12-05
 */
@Controller
@RequestMapping("groupType")
public class GroupTypeController extends BaseController<GroupTypeBiz,GroupType> {

}
