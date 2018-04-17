package com.example.wudelin.forestterritory.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wudelin.forestterritory.R;
import com.example.wudelin.forestterritory.utils.Logger;
import com.example.wudelin.forestterritory.utils.ShareUtil;
import com.example.wudelin.forestterritory.utils.StaticClass;
import com.example.wudelin.forestterritory.utils.ToastUtil;
import com.example.wudelin.forestterritory.utils.UtilTools;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.ui
 * 创建者：   wdl
 * 创建时间： 2018/4/13 14:08
 * 描述：    个人详细信息
 */

public class InformationActivity extends BaseActivity implements View.OnClickListener {
    //退出登录
    private Button btnLogout;
    //选择头像
    private RelativeLayout ll_personal_select_head;
    //修改昵称
    private RelativeLayout ll_personal_update_nn;
    //昵称
    private TextView tv_nn;
    //绑定手机
    private RelativeLayout rl_bound_phone;
    //手机
    private TextView tv_set_phone;
    //绑定邮箱
    private RelativeLayout rl_bound_email;
    //邮箱
    private TextView tv_set_email;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inf_set);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    //初始化用户信息
    private void initData() {
        HttpParams params = new HttpParams();
        String username = ShareUtil.getString(this,StaticClass.USERNAME,"");
        String password = ShareUtil.getString(this,StaticClass.PASSWORD,"");
        Logger.e("username:"+username+"  "+"password:"+password);
        params.put("uUsername",username);
        params.put("uPassword", UtilTools.EncoderByMd5(password));
        RxVolley.post(StaticClass.LOGIN_API, params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Logger.e("getData:"+t);
                if(!TextUtils.isEmpty(t)&&t.length()>0&&!t.equals("fail")){
                    parseJson(t);
                }else {
                    ToastUtil.showByStr(InformationActivity.this,
                            getString(R.string.get_fail));
                }
            }
        });
    }

    private void parseJson(String t) {
        try {
            Logger.e("center:"+t);
            JSONObject jsonObject = new JSONObject(t);
            tv_nn.setText(jsonObject.getString("uName"));
            tv_set_phone.setText(jsonObject.getString("uTelephone"));
            tv_set_email.setText(jsonObject.getString("uEmail"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(this);
        ll_personal_select_head = findViewById(R.id.ll_personal_select_head);
        ll_personal_select_head.setOnClickListener(this);
        ll_personal_update_nn = findViewById(R.id.ll_personal_update_nn);
        ll_personal_update_nn.setOnClickListener(this);
        tv_nn = findViewById(R.id.tv_nn);
        rl_bound_phone = findViewById(R.id.rl_bound_phone);
        rl_bound_phone.setOnClickListener(this);
        tv_set_phone = findViewById(R.id.tv_set_phone);
        rl_bound_email = findViewById(R.id.rl_bound_email);
        rl_bound_email.setOnClickListener(this);
        tv_set_email = findViewById(R.id.tv_set_email);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_logout:
                ShareUtil.delAll(InformationActivity.this);
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
            //选择头像
            case R.id.ll_personal_select_head:
                break;
            //修改昵称
            case R.id.ll_personal_update_nn:
                Intent intent = new Intent(this,UpdateInfActivity.class);
                intent.putExtra("update","name");
                startActivity(intent);
                break;
            //绑定手机号
            case R.id.rl_bound_phone:
                Intent intent1 = new Intent(this,UpdateInfActivity.class);
                intent1.putExtra("update","phone");
                startActivity(intent1);
                break;
            //绑定邮箱
            case R.id.rl_bound_email:
                Intent intent2 = new Intent(this,UpdateInfActivity.class);
                intent2.putExtra("update","email");
                startActivity(intent2);
                break;
            default:
        }
    }
}
