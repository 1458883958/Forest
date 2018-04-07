package com.example.wudelin.forestterritory.utils;

import android.util.Log;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.utils
 * 创建者：   wdl
 * 创建时间： 2018/4/5 20:13
 * 描述：    Log管理类
 */

public class Logger {
    //控制标记
    private static final boolean flag = true;
    //log 名称
    private static final String TAG = "smwdl";

    //d,i,e,w,v
    public static void d(String text){
        if(flag)
            Log.d(TAG, "d: "+text);
    }
    public static void i(String text){
        if(flag)
            Log.i(TAG, "d: "+text);
    }
    public static void e(String text){
        if(flag)
            Log.e(TAG, "d: "+text);
    }
    public static void w(String text){
        if(flag)
            Log.w(TAG, "d: "+text);
    }
    public static void v(String text){
        if(flag)
            Log.v(TAG, "d: "+text);
    }
}
