package com.example.wudelin.forestterritory.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.utils
 * 创建者：   wdl
 * 创建时间： 2018/4/5 20:18
 * 描述：    SharedPreferences封装
 */

public class ShareUtil {
    private static final String NAME = "config";
    //存String
    public static void putString(Context mContext, String key, String value){
        SharedPreferences sp =
                mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,value).apply();
    }
    //存int
    public static void putInt(Context mContext, String key, int value){
        SharedPreferences sp =
                mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key,value).apply();
    }
    //存boolean
    public static void putBoolean(Context mContext, String key, boolean value){
        SharedPreferences sp =
                mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key,value).apply();
    }

    //取String
    public static String getString(Context mContext,String key,String defValue){
        SharedPreferences sp =
                mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }
    //取INT
    public static int getInt(Context mContext,String key,int defValue){
        SharedPreferences sp =
                mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sp.getInt(key,defValue);
    }
    //取boolean
    public static boolean getBoolean(Context mContext,String key,boolean defValue){
        SharedPreferences sp =
                mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sp.getBoolean(key,defValue);
    }

    //删除单个
    public static void del(Context mContext,String key){
        SharedPreferences sp =
                mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key).apply();
    }
    //删除全部
    public static void delAll(Context mContext){
        SharedPreferences sp = mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().apply();
    }
}
