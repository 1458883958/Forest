package com.example.wudelin.forestterritory.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import com.ndktools.javamd5.Mademd5;


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

    public static String EncoderByMd5(String str) {
        Mademd5 md = new Mademd5();
        String newstr = md.toMd5(str);
        return newstr;
    }

}
