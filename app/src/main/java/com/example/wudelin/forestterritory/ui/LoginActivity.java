package com.example.wudelin.forestterritory.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wudelin.forestterritory.R;
import com.example.wudelin.forestterritory.utils.Logger;
import com.example.wudelin.forestterritory.utils.ShareUtil;
import com.example.wudelin.forestterritory.utils.StaticClass;
import com.example.wudelin.forestterritory.utils.ToastUtil;
import com.example.wudelin.forestterritory.utils.UtilTools;
import com.example.wudelin.forestterritory.view.CustomDialog;
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
    //Dialog
    private CustomDialog dialog;
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
        dialog = new CustomDialog(this, 100, 100,
                R.layout.dialog_loading, R.style.Theme_girl,
                Gravity.CENTER,R.style.pop_anim_style);
        //屏幕外点击无效
        dialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            //登录
            case R.id.btn_login:

                startLogin();
                //调试
                //startActivity(new Intent(this,MainActivity.class));
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
        final String password = etLoginPassword.getText().toString().trim();
        String url = StaticClass.ALY_IP
                +"/selectUsernameIf.do?uUsername="+username;
        Logger.e(url);
        if(!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(password)){
            dialog.show();
            RxVolley.get(url, new HttpCallback() {
                @Override
                public void onSuccess(String t) {
                    Logger.e(UtilTools.EncoderByMd5(password));
                    if(!TextUtils.isEmpty(t)&&UtilTools.EncoderByMd5(password).equals(t)) {
                        //成功
                        startActivity(new Intent(LoginActivity.this,
                                MainActivity.class));
                        ShareUtil.putString(LoginActivity.this,
                                StaticClass.USERNAME, username);
                        finish();
                    }else if (t.equals("nofind")){
                        ToastUtil.showByStr(LoginActivity.this,
                                "用户不存在");
                    } else{
                        //失败
                        ToastUtil.showByStr(LoginActivity.this,
                                "登录失败");
                    }
                    dialog.cancel();
                }

                @Override
                public void onFailure(VolleyError error) {
                    //失败
                    ToastUtil.showByStr(LoginActivity.this,
                            error.toString());
                    Logger.e(error.getMessage());
                    dialog.cancel();
                }
            });
        }
    }
}
