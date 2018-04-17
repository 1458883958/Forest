package com.example.wudelin.forestterritory.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.wudelin.forestterritory.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.ui
 * 创建者：   wdl
 * 创建时间： 2018/4/7 19:11
 * 描述：    注册
 */

public class RegActivity extends BaseActivity implements View.OnClickListener {
    private MaterialEditText metPhoneNumber;
    private MaterialEditText metPassword;
    private Button btnRegNext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        initView();
    }

    private void initView() {
        metPhoneNumber = findViewById(R.id.et_reg_phone_number);
        metPassword = findViewById(R.id.et_reg_password);
        btnRegNext = findViewById(R.id.btn_reg_next);
        btnRegNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_reg_next:
                startReg();
                break;
            default:
        }
    }


    private void startReg() {
        String number = metPhoneNumber.getText().toString().trim();
        String password = metPassword.getText().toString().trim();
        if(!TextUtils.isEmpty(number)&&!TextUtils.isEmpty(password)&&metPhoneNumber.isValid("\\d+")) {
            Intent intent = new Intent(this, SafetyVerificationActivity.class);
            intent.putExtra(SafetyVerificationActivity.PHONE_NUMBER,number);
            intent.putExtra(SafetyVerificationActivity.PASSWORD,password);
            intent.putExtra("from","reg");
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this,getString(R.string.valid_phone_number),Toast.LENGTH_SHORT).show();
        }
    }
}
