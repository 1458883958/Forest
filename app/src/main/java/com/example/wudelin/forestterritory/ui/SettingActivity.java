package com.example.wudelin.forestterritory.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wudelin.forestterritory.R;
import com.example.wudelin.forestterritory.utils.ShareUtil;
import com.example.wudelin.forestterritory.utils.StaticClass;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.ui
 * 创建者：   wdl
 * 创建时间： 2018/4/8 18:08
 * 描述：    个人中心
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout llPersonalInf;
    private RelativeLayout rlAboutApp;
    private TextView tvName;
    private CircleImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        llPersonalInf = findViewById(R.id.ll_personal_inf);
        llPersonalInf.setOnClickListener(this);
        rlAboutApp = findViewById(R.id.rl_about_app);
        rlAboutApp.setOnClickListener(this);
        tvName = findViewById(R.id.tv_set_username);
        imageView = findViewById(R.id.profile_image);
        String name = ShareUtil.getString(this,StaticClass.USERNAME,null);
        String url = ShareUtil.getString(this,StaticClass.HEAD_URL,null);
        tvName.setText(name);
        if(!TextUtils.isEmpty(url)) {
            Glide.with(this).load(url).into(imageView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_personal_inf:
                startActivityForResult(new Intent(this,
                        InformationActivity.class),0);
                break;
            case R.id.rl_about_app:
                startActivity(new Intent(this,AboutActivity.class));
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0){
            switch (resultCode){
                case 0:
                    //String url = data.getStringExtra("newUrl");
                    break;
                default:
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
