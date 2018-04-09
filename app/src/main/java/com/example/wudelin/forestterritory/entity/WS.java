package com.example.wudelin.forestterritory.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.entity
 * 创建者：   wdl
 * 创建时间： 2018/4/9 19:59
 * 描述：    TODO
 */

public class WS {
    private int bg;
    @SerializedName("cw")
    private List<CW> cwList;

    public WS(int bg, List<CW> cwList) {
        this.bg = bg;
        this.cwList = cwList;
    }

    public int getBg() {
        return bg;
    }

    public void setBg(int bg) {
        this.bg = bg;
    }

    public List<CW> getCwList() {
        return cwList;
    }

    public void setCwList(List<CW> cwList) {
        this.cwList = cwList;
    }
}
