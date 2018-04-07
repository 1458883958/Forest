package com.example.wudelin.forestterritory.entity;


/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.entity
 * 创建者：   wdl
 * 创建时间： 2018/4/7 21:44
 * 描述：    用户实体类
 */

public class User {
    //账户
    private String uUsername;
    //密码
    private String uPassword;
    //昵称
    private String uName;
    //tel
    private String uTelePhone;
    //所在地
    private String ipAddress;
    //邮箱
    private String email;
    public User(){}
    public User(String uUsername, String uPassword) {
        this.uUsername = uUsername;
        this.uPassword = uPassword;
    }

    public String getuUsername() {
        return uUsername;
    }

    public void setuUsername(String uUsername) {
        this.uUsername = uUsername;
    }

    public String getuPassword() {
        return uPassword;
    }

    public void setuPassword(String uPassword) {
        this.uPassword = uPassword;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuTelePhone() {
        return uTelePhone;
    }

    public void setuTelePhone(String uTelePhone) {
        this.uTelePhone = uTelePhone;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



}
