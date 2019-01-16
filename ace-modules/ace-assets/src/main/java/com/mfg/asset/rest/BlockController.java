package com.mfg.asset.rest;

import com.mfg.asset.biz.BlockBiz;
import com.mfg.asset.entity.Block;
import com.mfg.auth.common.rest.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("block")
public class BlockController extends BaseController<BlockBiz, Block> {


}
