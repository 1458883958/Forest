package com.example.wudelin.forestterritory.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.example.wudelin.forestterritory.R;
import com.example.wudelin.forestterritory.utils.Logger;
import com.example.wudelin.forestterritory.utils.ShareUtil;
import com.example.wudelin.forestterritory.utils.StaticClass;
import com.example.wudelin.forestterritory.utils.ToastUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.ui
 * 创建者：   wdl
 * 创建时间： 2018/4/17 18:44
 * 描述：    修改个人详细信息
 */

public class UpdateInfActivity extends BaseActivity{
    private MaterialEditText edContent;
    private Button btnUpdate;
    private int type;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_per);
        initView();
    }

    private void initView() {
        edContent = findViewById(R.id.medt_content);
        String updateType = getIntent().getStringExtra("update");
        if(!TextUtils.isEmpty(updateType)){
            if(updateType.equals("name")){
                setData(R.string.update_per_nname,R.string.enter_nname);
                type = 1;
            }
            else if(updateType.equals("phone")){
                setData(R.string.update_per_phone,R.string.enter_phone);
                type = 2;
            }
            else if(updateType.equals("email")){
                setData(R.string.update_per_email,R.string.enter_email);
                type = 3;
            }
        }
        btnUpdate = findViewById(R.id.btn_update_per);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edContent.getText().toString().trim();
                if(!TextUtils.isEmpty(content)){
                    updateDara(content);
                }else{
                    ToastUtil.showById(UpdateInfActivity.this,
                            R.string.check_content);
                }
            }
        });
    }

    private void updateDara(String content) {
        HttpParams params = new HttpParams();
        String uId = ShareUtil.getString(this,"uId","");
        Logger.e("uId:"+uId);
        params.put("uId",uId);
        if(type==1){
            params.put("uName",content);
        }else if(type==2){
            params.put("uTelephone",content);
        }
        else if(type==3){
            params.put("uEmail",content);
        }
        RxVolley.post(StaticClass.UPDATE_API, params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                if(t.equals("success")&&!TextUtils.isEmpty(t)){
                    ToastUtil.showByStr(UpdateInfActivity.this,
                            getString(R.string.successfully_modified));
                    finish();
                }else{
                    ToastUtil.showByStr(UpdateInfActivity.this,
                            getString(R.string.fail_modified));
                }
            }
        });
    }

    private void setData(int title, int helper) {
        getSupportActionBar().setTitle(getString(title));
        edContent.setHelperText(getString(helper));
    }
}
