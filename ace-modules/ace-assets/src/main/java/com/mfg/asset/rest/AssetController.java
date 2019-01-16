package com.mfg.asset.rest;

import com.mfg.asset.Vo.ShenChaVo;
import com.mfg.asset.biz.AssetBiz;
import com.mfg.asset.entity.Asset;
import com.mfg.asset.utils.QrCodeUtil;
import com.mfg.auth.common.msg.ObjectRestResponse;
import com.mfg.auth.common.rest.BaseController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("asset")
public class AssetController extends BaseController<AssetBiz, Asset> {


    @RequestMapping(value = "/qrCode/{id}", method = RequestMethod.POST)
    public ObjectRestResponse<Asset> handleQrCode(@PathVariable int id) {
        ObjectRestResponse<Asset> entityObjectRestResponse = new ObjectRestResponse<Asset>();
        Asset asset = baseBiz.selectById(id);
        String assetQrcodeAddress = asset.getAssetQrcodeAddress();
        //如果没有二维码，生成二维码
        if (null == assetQrcodeAddress || "".equals(assetQrcodeAddress) || "no".equals(assetQrcodeAddress)) {
            String url = QrCodeUtil.BASE_URL + asset.getSerialNumber();
            String qrCodeAddress = QrCodeUtil.createQrCode(url);
            asset.setAssetQrcodeAddress(qrCodeAddress);
            baseBiz.updateSelectiveById(asset);
        }
        entityObjectRestResponse.setData(asset);
        return entityObjectRestResponse;
    }

    @RequestMapping(value = "/shencha", method = RequestMethod.POST)
    public ObjectRestResponse<ShenChaVo> handleScanQrcode(@RequestBody String serialNumber) {
        ShenChaVo shenChaVo = baseBiz.getShenChaInfo(serialNumber);
        ObjectRestResponse<ShenChaVo> entityObjectRestResponse = new ObjectRestResponse<ShenChaVo>();
        entityObjectRestResponse.setData(shenChaVo);
        return entityObjectRestResponse;

    }

}
