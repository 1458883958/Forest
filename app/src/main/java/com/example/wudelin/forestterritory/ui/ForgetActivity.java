package com.example.wudelin.forestterritory.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.example.wudelin.forestterritory.R;
import com.example.wudelin.forestterritory.utils.ToastUtil;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.ui
 * 创建者：   wdl
 * 创建时间： 2018/4/7 19:07
 * 描述：    忘记密码
 */

public class ForgetActivity extends BaseActivity implements View.OnClickListener {
    private MaterialEditText editText;
    private Button btnNext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        initView();
    }

    private void initView() {
        editText = findViewById(R.id.et_forget_infor);
        btnNext = findViewById(R.id.btn_for_next);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_for_next:
                String username = editText.getText().toString().trim();
                if(!TextUtils.isEmpty(username)&&username.length()==11){
                    Intent intent = new Intent(this,
                            SafetyVerificationActivity.class);
                    intent.putExtra(SafetyVerificationActivity.PHONE_NUMBER,username);
                    intent.putExtra("from","forget");
                    startActivity(intent);
                    finish();
                }else{
                    ToastUtil.showById(this,R.string.valid_phone_number);
                }
                break;
            default:
        }
    }
}
