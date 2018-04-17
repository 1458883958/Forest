package com.example.wudelin.forestterritory.utils;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.utils
 * 创建者：   wdl
 * 创建时间： 2018/4/5 20:16
 * 描述：    数据/常量管理类
 */

public class StaticClass {

    //QQ登陆的头像URL
    public static final String HEAD_URL = "head_url";
    //判断App登陆状态,默认false
    public static final String IS_LOGIN = "isLogin";
    //腾讯Bugly app id
    public static final String BUGLY_APP_ID = "61716089ed";
    //闪屏页延时
    public static final int SPLASH_DELAY = 1001;
    //用户名
    public static final String USERNAME = "username";
    //密码
    public static final String PASSWORD = "password";
    //判断程序是否第一次运行
    public static final String IS_RUNNING_FIRST = "isFirst";
    //服务器ip
    public static final String ALY_IP = "http://www.xmhhs.top";
    //注册
    public static final String REG_API = ALY_IP+"/insertUserIf.do";
    //登录
    public static final String LOGIN_API = ALY_IP+"/selectUserIf.do";
    //保存树莓派信息
    public static final String SAVE_PI_API = ALY_IP+"/insertPiIf.do";
    //根据手机号修改密码
    public static final String PHONE_UPDATE_PSD = ALY_IP+"/updateByUsernameIf.do";
    //根据ip查询已经绑定的树莓派设备
    public static final String SELECT_RASP = ALY_IP+"/selectPiIf.do";
    //根据pId解除已绑定的树莓派设备
    public static final String DELETE_RASP = ALY_IP+"/deletePiIf.do";
    //历史图片
    public static final String HIS_PICTURE_API = ALY_IP+"/getPhotoPathIf.do";
    //控制树莓派开
    public static final String ON_PI = ALY_IP+"/onPiIf.do";
    //控制树莓派关
    public static final String OFF_PI = ALY_IP+"/offPiIf.do";
    //修改树莓派信息
    public static final String UPDATE_PI = ALY_IP+"/updatePiIf.do";
    //TTS appid
    public static final String TTS_APP_ID = "5acb1eb7";

    //树莓派列表
    //http://xmhhs.top/piListIf.do?uId=44
    public static final String LIST_PI = ALY_IP+"/piListIf.do";

    public static final String GIRL_API = "http://gank.io/api/random/data/"+UtilTools.encode()+"/50";
}
