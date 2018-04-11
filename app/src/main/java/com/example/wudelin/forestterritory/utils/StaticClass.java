package com.example.wudelin.forestterritory.utils;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.utils
 * 创建者：   wdl
 * 创建时间： 2018/4/5 20:16
 * 描述：    数据/常量管理类
 */

public class StaticClass {

    //腾讯Bugly app id
    public static final String BUGLY_APP_ID = "61716089ed";
    //闪屏页延时
    public static final int SPLASH_DELAY = 1001;
    //用户名
    public static final String USERNAME = "username";
    //判断程序是否第一次运行
    public static final String IS_RUNNING_FIRST = "isFirst";
    //服务器ip
    public static final String ALY_IP = "http://www.xmhhs.top";
    //注册
    public static final String REG_API = ALY_IP+"/insertUserIf.do?uUsername=?&uPassword=?";
    //登录
    public static final String LOGIN_API = ALY_IP+"/selectUsernameIf.do?uUsername=?";
    //历史图片
    public static final String HIS_PICTURE_API = ALY_IP+"/getmsg.do";
    //TTS appid
    public static final String TTS_APP_ID = "5acb1eb7";

    //美女社区图片接口
    public static final String GIRL_API = "http://gank.io/api/random/data/"+UtilTools.encode()+"/50";
}
