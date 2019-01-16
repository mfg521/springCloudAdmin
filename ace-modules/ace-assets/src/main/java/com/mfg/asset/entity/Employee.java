package com.mfg.asset.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "employee")
public class Employee implements Serializable {
    @Id
    private Integer employeeId;

    private String employeeUrl;
    private String employeeName;
    private String employeeUuid;
    private Date joinedDate;
    private String department;
    private String sponsor;
    private String designation;

    private String emailAddress;
    private String employeeClass;
    private String internalNo;
    private String beijingNo;
    private String dubaiNo;
    private String dubaiMobileNo;

    private Integer floor;
    private String roomNum;  //房间号
    private Integer blockId; //区块号
    private Integer xindex;
    private Integer yindex;
}
