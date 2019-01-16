package com.mfg.asset.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "record")
@Data
public class Record implements Serializable {
    private Integer recordId;
    private Integer employeeId;
    private String employeeUuid;
    private String taggerNumber;

    @Column(name = "borrowed_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.time.LocalDateTime borrowedDate;

    @Column(name = "return_date")
    @ApiModelProperty(value = "归还日期 ")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.time.LocalDateTime returnDate;

    private String serialNumber;
    private String assetType;
    private Integer isReturned;
}
