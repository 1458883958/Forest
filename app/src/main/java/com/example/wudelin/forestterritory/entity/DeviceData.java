package com.example.wudelin.forestterritory.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.entity
 * 创建者：   wdl
 * 创建时间： 2018/4/8 19:26
 * 描述：    设备实体类
 */

public class DeviceData implements Parcelable {

    //设备pId
    private String pId;

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

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

    public DeviceData() { //构造
    }
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

    //必须实现的方法
    public static final Creator<DeviceData> CREATOR = new Creator<DeviceData>() {
        @Override
        public DeviceData createFromParcel(Parcel in) {
            DeviceData data = new DeviceData();
            data.pName = in.readString();
            data.pRemark = in.readString();
            data.pIpaddress = in.readString();
            data.pBootsate = in.readString();
            data.pSwitchstate = in.readString();
            data.pThreshold = in.readDouble();
            data.pDelayTime = in.readInt();
            return data;
        }

        @Override
        public DeviceData[] newArray(int size) {
            return new DeviceData[size];
        }
    };

    @Override
    public int describeContents() {  //重写的方法
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pName);
        dest.writeString(pRemark);
        dest.writeString(pIpaddress);
        dest.writeString(pBootsate);
        dest.writeString(pSwitchstate);
        dest.writeDouble(pThreshold);
        dest.writeInt(pDelayTime);
    }
}
