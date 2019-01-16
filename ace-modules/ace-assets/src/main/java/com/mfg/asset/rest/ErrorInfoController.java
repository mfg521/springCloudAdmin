package com.mfg.asset.rest;

import com.mfg.asset.biz.ErrorInfoBiz;
import com.mfg.asset.entity.ErrorInfo;
import com.mfg.auth.common.msg.ObjectRestResponse;
import com.mfg.auth.common.rest.BaseController;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("error")
public class ErrorInfoController extends BaseController<ErrorInfoBiz, ErrorInfo> {

    @Override
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<ErrorInfo> add(@RequestBody ErrorInfo entity) {
        System.out.println(entity.toString());
        Integer submitType=entity.getSubmitType();
        // 0代表的是错误，如果是0的话，将当前是否解决改成0
        // 1则表示不做特殊的处理，按照数据库的默认即可
        if(null==submitType || "".equals(submitType)){
            entity.setIsSolved(1);
        }else {
            entity.setSubmitType(0);
            entity.setIsSolved(0);
        }
        baseBiz.insertSelective(entity);
        return new ObjectRestResponse<ErrorInfo>();
    }
}
