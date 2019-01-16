package com.mfg.asset.rest;

import com.mfg.asset.biz.RecordBiz;
import com.mfg.asset.commom.ApiResult;
import com.mfg.asset.commom.ParameterNotValidException;
import com.mfg.asset.entity.Record;
import com.mfg.auth.common.rest.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "record")
public class RecordController extends BaseController<RecordBiz, Record> {

    @Autowired
    private RecordBiz recordBiz;


    @RequestMapping(value = "/returnAsset", method = RequestMethod.POST)
    public ApiResult<Integer> returnAsset(@RequestBody Record record) {
        return ApiResult.success(recordBiz.returnAsset(record));
    }

    @RequestMapping(value = "/changeAsset", method = RequestMethod.POST)
    public ApiResult<Integer> changeAsset(@RequestBody Map recordVo) throws ParameterNotValidException {
        return ApiResult.success(recordBiz.changeOrSaveAsset(recordVo));
    }

    @RequestMapping(value = "/getAsset", method = RequestMethod.POST)
    public List<Record> findAsset(@RequestBody Record record) {
        return recordBiz.findAsset(record);
    }


}
