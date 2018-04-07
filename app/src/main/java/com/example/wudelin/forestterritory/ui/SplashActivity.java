package com.example.wudelin.forestterritory.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.wudelin.forestterritory.R;
import com.example.wudelin.forestterritory.utils.StaticClass;
import com.example.wudelin.forestterritory.utils.UtilTools;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.ui
 * 创建者：   wdl
 * 创建时间： 2018/4/7 15:43
 * 描述：    启动页
 */

public class SplashActivity extends AppCompatActivity{
    private TextView tvSplashText;

    /*
   * 1.延时2000
   * 2.自定义字体
   * 3.全屏主题
   * */
    @SuppressLint("HandlerLeak")
    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case StaticClass.SPLASH_DELAY:
                    startActivity(new Intent(SplashActivity.this,
                            LoginActivity.class));
                    finish();
                    break;
                default:
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }
    //初始化
    private void initView() {
        tvSplashText = findViewById(R.id.tv_splash);
        //延迟4秒
        myHandler.sendEmptyMessageDelayed(StaticClass.SPLASH_DELAY,3000);
        //设置字体
        UtilTools.setFont(this,tvSplashText);
    }
    //禁止返回键
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
