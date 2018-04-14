package com.example.wudelin.forestterritory.entity;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.entity
 * 创建者：   wdl
 * 创建时间： 2018/4/8 19:26
 * 描述：    设备实体类
 */

public class DeviceData {
    //设备名称
    private String pName;
    //设备ip
    private String pIpaddress;
    //备注
    private String pRemark;
    //延时拍照的时间
    private int pDelayTime;
    //阈值
    private Double pThreshold;
    //是否运行
    private String pSwitchstate;
    //是否开机
    private String pBootsate;

    public int getpDelayTime() {
        return pDelayTime;
    }

    public void setpDelayTime(int pDelayTime) {
        this.pDelayTime = pDelayTime;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpIpaddress() {
        return pIpaddress;
    }

    public void setpIpaddress(String pIpaddress) {
        this.pIpaddress = pIpaddress;
    }

    public String getpRemark() {
        return pRemark;
    }

    public void setpRemark(String pRemark) {
        this.pRemark = pRemark;
    }

    public Double getpThreshold() {
        return pThreshold;
    }

    public void setpThreshold(Double pThreshold) {
        this.pThreshold = pThreshold;
    }

    public String getpSwitchstate() {
        return pSwitchstate;
    }

    public void setpSwitchstate(String pSwitchstate) {
        this.pSwitchstate = pSwitchstate;
    }

    public String getpBootsate() {
        return pBootsate;
    }

    public void setpBootsate(String pBootsate) {
        this.pBootsate = pBootsate;
    }
}
