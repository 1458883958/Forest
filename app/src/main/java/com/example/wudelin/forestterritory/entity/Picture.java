package com.example.wudelin.forestterritory.entity;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.entity
 * 创建者：   wdl
 * 创建时间： 2018/4/8 20:06
 * 描述：    图片实体类
 */

public class Picture {
    private String ivUrl;
    private String time;

    public Picture(String ivUrl, String time) {
        this.ivUrl = ivUrl;
        this.time = time;
    }

    public String getIvUrl() {
        return ivUrl;
    }

    public void setIvUrl(String ivUrl) {
        this.ivUrl = ivUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
