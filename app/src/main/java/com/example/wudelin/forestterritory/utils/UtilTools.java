package com.example.wudelin.forestterritory.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import com.example.wudelin.forestterritory.R;
import com.example.wudelin.forestterritory.application.MyApplication;
import com.ndktools.javamd5.Mademd5;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.utils
 * 创建者：   wdl
 * 创建时间： 2018/4/7 15:55
 * 描述：    通用工具类
 */

public class UtilTools {

    //设置字体
    public static void setFont(Context mContent, TextView textView){
        Typeface fontType = Typeface.
                createFromAsset(mContent.getAssets(),"fonts/FONT.TTF");
        textView.setTypeface(fontType);
    }

    //MD5加密
    public static String EncoderByMd5(String str) {
        Mademd5 md = new Mademd5();
        String newstr = md.toMd5(str);
        return newstr;
    }

    //转码
    public static String encode(){
        String welfare = null;
        try {
            welfare = URLEncoder.encode(MyApplication.context.getString(R.string.welfare),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return welfare;
    }

}
