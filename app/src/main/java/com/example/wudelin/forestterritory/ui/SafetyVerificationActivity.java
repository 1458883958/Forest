package com.example.wudelin.forestterritory.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wudelin.forestterritory.R;
import com.example.wudelin.forestterritory.utils.Logger;
import com.example.wudelin.forestterritory.utils.StaticClass;
import com.example.wudelin.forestterritory.utils.ToastUtil;
import com.example.wudelin.forestterritory.utils.UtilTools;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.ui
 * 创建者：   wdl
 * 创建时间： 2018/4/7 19:27
 * 描述：    安全验证
 */

public class SafetyVerificationActivity extends BaseActivity implements View.OnClickListener {
    public static final String PHONE_NUMBER = "phone_number";
    public static final String PASSWORD = "password";
    private TextView tvSafeLable;
    private TextView resend;
    private Button btnNext;
    private EditText edCode;
    private String phoneNumber;
    private String password;
    private Handler mHandler = new Handler();
    private int T = 60;
    //标记是注册传来 的  还是忘记密码传来的
    private int flag;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe);
        initView();

    }

    private void initView() {

        Intent intent = getIntent();
        String form = intent.getStringExtra("from");
        if(form.equals("reg")){
            flag = 1;
            phoneNumber = intent.getStringExtra(PHONE_NUMBER);
            password = intent.getStringExtra(PASSWORD);
        }
        if (form.equals("forget")){
            flag = 2;
            phoneNumber = intent.getStringExtra(PHONE_NUMBER);
            password = "";
        }
        sendCode("86",phoneNumber);
        tvSafeLable = findViewById(R.id.tv_safe_lable);
        btnNext = findViewById(R.id.btn_safe_next);
        btnNext.setOnClickListener(this);
        resend = findViewById(R.id.resend);
        resend.setOnClickListener(this);
        new Thread(new MyCountDownTimer()).start();//开始执行
        edCode = findViewById(R.id.safe_code);
        tvSafeLable.setText("请输入"+
                phoneNumber.substring(0,3)+"****"+phoneNumber.substring(7,11)
                +"收到的短信验证码。");

    }

    // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
    public void sendCode(String country, String phone) {
        // 注册一个事件回调，用于处理发送验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理成功得到验证码的结果
                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                } else{
                    // TODO 处理错误的结果
                }

            }
        });
        // 触发操作
        SMSSDK.getVerificationCode(country, phone);
    }


    // 提交验证码，其中的code表示验证码，如“1357”
    public void submitCode(String country, String phone, String code) {
        // 注册一个事件回调，用于处理提交验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理验证成功的结果
                    Logger.d("afterEvent: "+"注册成功");
                    if(flag==1)
                        startReg();
                    if(flag==2)
                        startForget();

                } else{
                    // TODO 处理错误的结果
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SafetyVerificationActivity.this,
                                    "验证码错误",Toast.LENGTH_SHORT).show();
                        }
                    });
                    Logger.d( "afterEvent: "+"注册失败");
                }

            }
        });
        // 触发操作
        SMSSDK.submitVerificationCode(country, phone, code);
    }

    //修改密码
    private void startForget() {
        Intent intent = new Intent(this,UpdatePsdActivity.class);
        intent.putExtra("username",phoneNumber);
        startActivity(intent);
    }

    //注册
    private void startReg() {
        HttpParams params = new HttpParams();
        params.put("uUsername",phoneNumber);
        params.put("uPassword",UtilTools.EncoderByMd5(password));

        RxVolley.post(StaticClass.REG_API, params,new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                if(t.equals("success")) {
                    startActivity(new Intent(SafetyVerificationActivity.this,
                            LoginActivity.class));
                    ToastUtil.showByStr(SafetyVerificationActivity.this,
                            "注册成功");
                    finish();
                }else{
                    ToastUtil.showByStr(SafetyVerificationActivity.this,
                            "注册失败");
                }
            }

            @Override
            public void onFailure(VolleyError error) {
                Toast.makeText(SafetyVerificationActivity.this,
                        ""+error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_safe_next:
                String code = edCode.getText().toString();
                if(!TextUtils.isEmpty(code)){
                    submitCode("86",phoneNumber,code);
                }else{
                    Toast.makeText(
                            this,"不能为空!"
                            ,Toast.LENGTH_SHORT
                    ).show();
                }
                break;
            case R.id.resend:
                new Thread(new MyCountDownTimer()).start();//开始执行
                sendCode("86",phoneNumber);
                break;
            default:
        }
    }
    /**
     * 自定义倒计时类，实现Runnable接口
     */
    class MyCountDownTimer implements Runnable{

        @Override
        public void run() {

            //倒计时开始，循环
            while (T > 0) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        resend.setClickable(false);
                        resend.setText(T + "秒后重新发送");
                    }
                });
                try {
                    Thread.sleep(1000); //强制线程休眠1秒，就是设置倒计时的间隔时间为1秒。
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                T--;
            }

            //倒计时结束，也就是循环结束
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    resend.setClickable(true);
                    resend.setText("重新发送");
                }
            });
            T = 60; //最后再恢复倒计时时长
        }
    }
}
