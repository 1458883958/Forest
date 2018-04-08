package com.example.wudelin.forestterritory.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wudelin.forestterritory.R;
import com.example.wudelin.forestterritory.utils.ShareUtil;
import com.example.wudelin.forestterritory.utils.StaticClass;
import com.example.wudelin.forestterritory.utils.ToastUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;
import com.rengwuxian.materialedittext.MaterialEditText;


/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.ui
 * 创建者：   wdl
 * 创建时间： 2018/4/7 16:06
 * 描述：    登陆
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private MaterialEditText etLoginUsername;
    private MaterialEditText etLoginPassword;
    private Button btnLogin;
    private TextView tvRemPsd;
    private TextView tvRegUsr;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

    }

    private void initView() {
        etLoginUsername = findViewById(R.id.et_login_username);
        etLoginPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        tvRemPsd = findViewById(R.id.tv_rem_psd);
        tvRemPsd.setOnClickListener(this);
        tvRegUsr = findViewById(R.id.tv_reg_usr);
        tvRegUsr.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            //登录
            case R.id.btn_login:
                //startLogin();
                //调试
                startActivity(new Intent(this,
                        MainActivity.class));
                break;
            //忘记密码
            case R.id.tv_rem_psd:
                startActivity(new Intent(LoginActivity.this,
                        ForgetActivity.class));
                break;
            //注册
            case R.id.tv_reg_usr:
                startActivity(new Intent(LoginActivity.this,
                        RegActivity.class));
                break;
            default:
        }
    }

    private void startLogin() {
        final String username = etLoginUsername.getText().toString().trim();
        String password = etLoginPassword.getText().toString().trim();
        if(!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(password)){
            RxVolley.get(StaticClass.LOGIN_API, new HttpCallback() {
                @Override
                public void onSuccess(String t) {
                    //成功
                    startActivity(new Intent(LoginActivity.this,
                            MainActivity.class));
                    ShareUtil.putString(LoginActivity.this,
                            StaticClass.USERNAME,username);
                    finish();
                }

                @Override
                public void onFailure(VolleyError error) {
                    //失败
                    ToastUtil.showByStr(LoginActivity.this,
                            error.getMessage());
                }
            });
        }
    }
}
