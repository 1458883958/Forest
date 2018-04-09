package com.example.wudelin.forestterritory.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.entity
 * 创建者：   wdl
 * 创建时间： 2018/4/9 19:45
 * 描述：    TTS语音实体类
 */

public class VoiceBean {
    private int sn;
    private boolean ls;
    private int bg;
    private int ed;
    @SerializedName("ws")
    private List<WS> wsList;

    public VoiceBean(int sn, boolean ls, int bg, int ed, List<WS> wsList) {
        this.sn = sn;
        this.ls = ls;
        this.bg = bg;
        this.ed = ed;
        this.wsList = wsList;
    }

    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

    public boolean isLs() {
        return ls;
    }

    public void setLs(boolean ls) {
        this.ls = ls;
    }

    public int getBg() {
        return bg;
    }

    public void setBg(int bg) {
        this.bg = bg;
    }

    public int getEd() {
        return ed;
    }

    public void setEd(int ed) {
        this.ed = ed;
    }

    public List<WS> getWsList() {
        return wsList;
    }

    public void setWsList(List<WS> wsList) {
        this.wsList = wsList;
    }
}
