package com.mfg.asset.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "asset")
@Data
public class Asset {

    @Id
    private Integer assetId;

    private String assetUuid;

    private String assetType;

    private Integer assetStatus;

    private String taggerNumber;

    private String beijingCode;

    private String financeCode;

    private String serialNumber;

    private String computerModel;

    private String assetQrcodeAddress;

}
