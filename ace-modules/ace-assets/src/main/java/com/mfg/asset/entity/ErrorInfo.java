package com.mfg.asset.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "checkrecord")
@Data
public class ErrorInfo {

    @Id
    private Integer id;

    private String serialNumber;

    private Integer roomNum;

    private String submitInfo;

    private String submitPeople;

    @Column(name = "submit_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.time.LocalDateTime submitDate;

    private Integer submitType;

    private Integer isSolved;
}
