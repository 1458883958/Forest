package com.example.wudelin.forestterritory.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.utils
 * 创建者：   wdl
 * 创建时间： 2018/4/7 22:08
 * 描述：    弹窗工具类
 */

public class ToastUtil {
    private static final int TIME = Toast.LENGTH_SHORT;
    public static void showByStr(Context mContent,String text){
        Toast.makeText(mContent,text,TIME).show();
    }
    public static void showById(Context mContent,int id){
        Toast.makeText(mContent,id,TIME).show();
    }
}
