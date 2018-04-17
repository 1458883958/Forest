package com.example.wudelin.forestterritory.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wudelin.forestterritory.R;
import com.example.wudelin.forestterritory.entity.DeviceData;
import com.example.wudelin.forestterritory.utils.Logger;
import com.example.wudelin.forestterritory.utils.StaticClass;
import com.example.wudelin.forestterritory.utils.ToastUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.ui
 * 创建者：   wdl
 * 创建时间： 2018/4/16 16:19
 * 描述：    设备详情/编辑
 */

public class DetialDeviceActivity extends BaseActivity implements View.OnClickListener {
    private MaterialEditText pieName;
    private MaterialEditText pieRemark;
    private MaterialEditText pieThreshold;
    private MaterialEditText pieDelaytime;
    private MaterialEditText pieIp;
    private MaterialEditText pieBootstate;
    private Button btnUpdate;
    private Button btnPic;
    private TextView tvSetting;
    private String pIpaddress;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_device);
        initView();
    }

    //设置具体的值
    private void initData() {

        //Logger.e("Detail:pid= "+pId);
        HttpParams params = new HttpParams();
        params.put("pIpaddress",pIpaddress);
        RxVolley.post(StaticClass.SELECT_RASP, params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                if(!TextUtils.isEmpty(t)&&!t.equals("fail")){
                    parseJson(t);
                }
            }
        });
    }

    private void parseJson(String t) {
        try {
            Logger.e("Detial:"+t);
            JSONObject jsonObject = new JSONObject(t);
            pieName.setText(jsonObject.getString("pName"));
            pieRemark.setText(jsonObject.getString("pRemark"));
            pieBootstate.setText(jsonObject.getString("pBootstate"));
            pieDelaytime.setText(jsonObject.getString("pDelayed"));
            pieIp.setText(jsonObject.getString("pIpaddress"));
            pieThreshold.setText(jsonObject.getString("pThreshold"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        pIpaddress = getIntent().getStringExtra("pIpaddress");
        pieName = findViewById(R.id.pie_name);
        pieRemark = findViewById(R.id.pie_remark);
        pieBootstate = findViewById(R.id.pie_bootstate);
        pieDelaytime = findViewById(R.id.pie_delaytime);
        pieIp = findViewById(R.id.pie_ipaddress);
        pieThreshold = findViewById(R.id.pie_threshold);
        btnPic = findViewById(R.id.btn_pic);
        btnPic.setOnClickListener(this);
        btnUpdate = findViewById(R.id.btn_make_update);
        btnUpdate.setOnClickListener(this);
        btnUpdate.setVisibility(View.GONE);
        tvSetting = findViewById(R.id.tv_btn_setting);
        tvSetting.setOnClickListener(this);
        setFoucs(false);
        initData();
    }

    private void setFoucs(boolean flag) {
        pieName.setEnabled(flag);
        pieRemark.setEnabled(flag);
        pieBootstate.setEnabled(false);
        pieDelaytime.setEnabled(flag);
        pieIp.setEnabled(false);
        pieThreshold.setEnabled(flag);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_make_update:
                Logger.e("btnOnclick:");
                String pName = pieName.getText().toString().trim();
                String pRemark = pieRemark.getText().toString().trim();
                String pDelaytime = pieDelaytime.getText().toString().trim();
                String pThreshold = pieThreshold.getText().toString().trim();
                //String pIp = pieIp.getText().toString().trim();
                String pId = getIntent().getStringExtra("pId");
                if(!TextUtils.isEmpty(pName)&&!TextUtils.isEmpty(pRemark)&&
                        !TextUtils.isEmpty(pDelaytime)&&!TextUtils.isEmpty(pThreshold)){
                    updateData(pId,pName,pRemark,pDelaytime,pThreshold);
                }else{
                    ToastUtil.showByStr(this,"请检查内容");
                }
                break;
            case R.id.tv_btn_setting:
                //设为可点击
                setFoucs(true);
                //确认修改按钮可见
                btnUpdate.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_pic:
                Intent intent = new Intent(this,PictureActivity.class);
                intent.putExtra("pIpaddress",pIpaddress);
                startActivity(intent);
                break;
            default:
        }

    }

    private void updateData(String pId, String pName, String pRemark, String pDelaytime, String pThreshold) {
        HttpParams params = new HttpParams();
        params.put("pId",pId);
        params.put("pName",pName);
        params.put("pRemark",pRemark);
        params.put("pDelayed",pDelaytime);
        params.put("pThreshold",pThreshold);
        RxVolley.post(StaticClass.UPDATE_PI, params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Logger.e("修改结果:"+t);
                if(t.equals("success")){
                    setFoucs(false);
                    btnUpdate.setVisibility(View.GONE);
                    ToastUtil.showByStr(DetialDeviceActivity.this,"修改成功");
                }else{
                    ToastUtil.showByStr(DetialDeviceActivity.this,"修改失败");
                }
            }
        });
    }
}
