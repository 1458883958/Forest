package com.example.wudelin.forestterritory.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.example.wudelin.forestterritory.R;
import com.example.wudelin.forestterritory.utils.StaticClass;
import com.example.wudelin.forestterritory.utils.ToastUtil;
import com.example.wudelin.forestterritory.utils.UtilTools;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.ui
 * 创建者：   wdl
 * 创建时间： 2018/4/10 20:54
 * 描述：    //修改密码
 */

public class UpdatePsdActivity extends BaseActivity implements View.OnClickListener {
    private MaterialEditText editText;
    private Button btnUpdNext;
    private String phoneNumber;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        initView();
    }

    private void initView() {
        editText = findViewById(R.id.et_update_psd);
        btnUpdNext = findViewById(R.id.btn_upd_next);
        btnUpdNext.setOnClickListener(this);
        phoneNumber = getIntent().getStringExtra("username");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_upd_next:
                String password = editText.getText().toString().trim();
                if(!TextUtils.isEmpty(password)&&password.length()>6){
                    startUpdate(password);
                }else{
                    ToastUtil.showById(this,R.string.length);
                }
                break;
            default:
        }
    }

    //修改密码
    private void startUpdate(String password) {
        HttpParams params = new HttpParams();
        params.put("uUsername",phoneNumber);;
        params.put("uPassword", UtilTools.EncoderByMd5(password));
        RxVolley.post(StaticClass.PHONE_UPDATE_PSD,params,new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                if(t.equals("success")){
                    startActivity(new Intent(UpdatePsdActivity.this,
                            LoginActivity.class));
                    finish();
                }else{
                    ToastUtil.showByStr(UpdatePsdActivity.this,"失败");
                }
            }
        });
    }
}
