package com.example.wudelin.forestterritory.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wudelin.forestterritory.R;
import com.example.wudelin.forestterritory.utils.UtilTools;
import com.tencent.bugly.beta.Beta;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.ui
 * 创建者：   wdl
 * 创建时间： 2018/4/13 16:34
 * 描述：    关于
 */

public class AboutActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rlUpdate;
    private TextView tvVersionCode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
    }

    private void initView() {
        rlUpdate = findViewById(R.id.rl_update);
        rlUpdate.setOnClickListener(this);
        tvVersionCode = findViewById(R.id.tv_version_code);
        tvVersionCode.setText("当前版本号:"+UtilTools.getVersionCode());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_update:
                Beta.checkUpgrade(true,false);
                break;
            default:
        }
    }
}
