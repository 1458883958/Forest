package com.example.wudelin.forestterritory.application;

import android.app.Application;

import com.example.wudelin.forestterritory.utils.StaticClass;
import com.mob.MobSDK;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.application
 * 创建者：   wdl
 * 创建时间： 2018/4/7 15:06
 * 描述：    Application类
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        //腾讯Bugly初始化
        /*
        * 第三个参数为SDK调试模式开关，调试模式的行为特性如下：
        * 输出详细的Bugly SDK的Log；
        * 每一条Crash都会被立即上报；
        * 自定义日志将会在Logcat中输出。
        * 建议在测试阶段建议设置成true，发布时设置为false。
        * */
        Bugly.init(getApplicationContext(),
                StaticClass.BUGLY_APP_ID, true);

        //短信验证
        MobSDK.init(this);
    }
}
