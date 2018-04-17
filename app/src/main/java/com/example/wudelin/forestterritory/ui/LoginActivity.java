package com.example.wudelin.forestterritory.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wudelin.forestterritory.R;
import com.example.wudelin.forestterritory.utils.Logger;
import com.example.wudelin.forestterritory.utils.ShareUtil;
import com.example.wudelin.forestterritory.utils.StaticClass;
import com.example.wudelin.forestterritory.utils.ToastUtil;
import com.example.wudelin.forestterritory.utils.UtilTools;
import com.example.wudelin.forestterritory.view.CustomDialog;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;


/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.ui
 * 创建者：   wdl
 * 创建时间： 2018/4/7 16:06
 * 描述：    登陆
 */
/*
*   你的应用是否具备独立账户系统？
*   这个问题是第三方登录时接口选择的重要标准。
*   如果你选择“是”，则意味着你的应用只是需要第三方平台的用户，
*   而不是他们的账户验证功能——也就是“要数据，不要功能”。
*   而如果你选择“否”，则表示你实际上是’“要功能，不要数据（用户）”’。
*   对于ShareSDK来说，前者你的入口方法是showUser(null)，而后者是authorize()。
*   那么下面我分情况解释两种接入方式的步骤。
* */

/**
 * ShareSDK第三方登录
 * 1.手动授权
 * 2.自动授权
 * 3.SSO授权：就是利用这些平台的手机客户端来完成授权。由于SSO的授权方式对于用户来说更加便捷，
 * 因此各大平台均建议开发者优先使用这一种授权方式。ShareSDK提供SSO的授权实现，并且默认情况下是使用的
 * <p>
 * platform.showUser(null);//授权并获取用户信息，具备独立账户系统,就是说你的应用自己就有注册和登录功能，要数据，不要功能
 * platform.authorize();//单独授权,OnComplete返回的hashmap是空的,不具备独立账户系统,要功能，不要数据（用户）
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, PlatformActionListener {

    private MaterialEditText etLoginUsername;
    private MaterialEditText etLoginPassword;
    private Button btnLogin;
    private TextView tvRemPsd;
    private TextView tvRegUsr;
    //qq登录
    private ImageButton btnLoginQq;
    //微信登录
    private ImageButton btnLoginWeixin;
    //Dialog
    private CustomDialog dialog;

    @SuppressLint("HandlerLeak")
    private Handler mHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(LoginActivity.this
                            , "授权成功", Toast.LENGTH_SHORT).show();
                    Platform platform = (Platform) msg.obj;
                    //账号
                    String userId = platform.getDb().getUserId();
                    //用户名
                    String userName = platform.getDb().getUserName();
                    //头像
                    String userIcon = platform.getDb().getUserIcon();
                    //性别
                    String userGender = platform.getDb().getUserGender();
                    Logger.e("userId: " + userId + "\n" +
                            "userName: " + userName + "\n" + "userIcon: " + userIcon + "\n" +
                            "userGender: " + userGender);
                    startReg(userId, userIcon);
                    break;
                case 2:
                    ToastUtil.showByStr(LoginActivity.this,
                            "授权失败");
                    break;
                case 3:
                    ToastUtil.showByStr(LoginActivity.this,
                            "授权取消");
                    break;
                default:
            }
        }
    };

    //QQ登录时保存信息
    private void startReg(final String userId,final String userIcon) {
        //传账号密码
        HttpParams params = new HttpParams();
        params.put("uUsername", userId);
        params.put("uPassword", UtilTools.EncoderByMd5("12345"));
        RxVolley.post(StaticClass.REG_API, params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Logger.e("qReg:"+t);
                startActivity(new Intent(LoginActivity.this,
                        MainActivity.class));
                ShareUtil.putString(LoginActivity.this, StaticClass.USERNAME,
                        userId);
                ShareUtil.putString(LoginActivity.this, StaticClass.PASSWORD,
                        "12345");
                ShareUtil.putString(LoginActivity.this, StaticClass.HEAD_URL,
                        userIcon);
                finish();
            }
        });

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

    }

    //初始化
    private void initView() {
        etLoginUsername = findViewById(R.id.et_login_username);
        etLoginPassword = findViewById(R.id.et_login_password);
        btnLoginQq = findViewById(R.id.btn_login_qq);
        btnLoginQq.setOnClickListener(this);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        tvRemPsd = findViewById(R.id.tv_rem_psd);
        tvRemPsd.setOnClickListener(this);
        tvRegUsr = findViewById(R.id.tv_reg_usr);
        tvRegUsr.setOnClickListener(this);
        dialog = new CustomDialog(this, 100, 100,
                R.layout.dialog_loading, R.style.Theme_dialog,
                Gravity.CENTER, R.style.pop_anim_style);
        //屏幕外点击无效
        dialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            //QQ登录
            case R.id.btn_login_qq:
                loginByQQ();
                break;
            default:
        }
    }

    //QQ登录
    private void loginByQQ() {
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.setPlatformActionListener(this);
        qq.SSOSetting(false);
        if (!qq.isAuthValid()) {
            ToastUtil.showByStr(this, "请前往安装QQ");
        }
        authorize(qq);
    }

    private void authorize(Platform qq) {
        if (qq == null)
            return;
        //如果授权就删除授权资料
        if (qq.isAuthValid()) {
            qq.removeAccount(true);
        }
        //授权并获取用户信息
        qq.showUser(null);
    }

    //登录,成功保存账号，密码，UID
    private void startLogin() {
        final String username = etLoginUsername.getText().toString().trim();
        final String password = etLoginPassword.getText().toString().trim();
        //传账号密码
        HttpParams params = new HttpParams();
        params.put("uUsername", username);
        params.put("uPassword", UtilTools.EncoderByMd5(password));
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            dialog.show();
            RxVolley.post(StaticClass.LOGIN_API, params, new HttpCallback() {
                @Override
                public void onSuccess(String t) {
                    //t为json，之后的查询根据uID查询
                    Logger.e("login:" + t);
                    if (t.equals("fail")) {
                        ToastUtil.showByStr(LoginActivity.this,
                                "登录失败");
                        dialog.cancel();
                    } else {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            dialog.cancel();
                        }
                        //解析json保存UID
                        parseJson(t);
                        //成功
                        startActivity(new Intent(LoginActivity.this,
                                MainActivity.class));
                        ShareUtil.putString(LoginActivity.this,
                                StaticClass.USERNAME, username);
                        ShareUtil.putString(LoginActivity.this,
                                StaticClass.PASSWORD, UtilTools.EncoderByMd5(password));
                        finish();
                    }
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

    private void parseJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            String uId = jsonObject.getString("uId");
            Logger.e("uId:"+uId);
            ShareUtil.putString(LoginActivity.this, "uId", uId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //授权成功回调
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Message message = Message.obtain();
        message.what = 1;
        message.obj = platform;
        mHanlder.sendMessage(message);
    }

    //授权失败回调
    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Message message = Message.obtain();
        message.what = 2;
        message.obj = platform;
        mHanlder.sendMessage(message);
    }

    //授权取消回调
    @Override
    public void onCancel(Platform platform, int i) {
        Message message = Message.obtain();
        message.what = 3;
        message.obj = platform;
        mHanlder.sendMessage(message);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
