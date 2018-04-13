package com.example.wudelin.forestterritory.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.example.wudelin.forestterritory.R;
import com.example.wudelin.forestterritory.utils.ShareUtil;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.ui
 * 创建者：   wdl
 * 创建时间： 2018/4/13 14:08
 * 描述：    个人详细信息
 */

public class InformationActivity extends BaseActivity implements View.OnClickListener {
    private Button btnLogout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inf_set);
        initView();
    }

    private void initView() {
        btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_logout:
                ShareUtil.delAll(InformationActivity.this);
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
            default:
        }
    }
}
