package com.example.wudelin.forestterritory.ui;


import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.example.wudelin.forestterritory.R;
import com.example.wudelin.forestterritory.fragment.ButlerFragment;
import com.example.wudelin.forestterritory.fragment.DealFragment;
import com.example.wudelin.forestterritory.fragment.DeviceFragment;
import com.example.wudelin.forestterritory.utils.Logger;
import com.example.wudelin.forestterritory.utils.ShareUtil;
import com.example.wudelin.forestterritory.utils.StaticClass;
import com.example.wudelin.forestterritory.utils.UtilTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<String> titleList;
    private List<Fragment> fragmentList;
    private FloatingActionButton fabSetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //去掉阴影
        getSupportActionBar().setElevation(0);
        //腾讯Bugly异常测试
        //CrashReport.testJavaCrash();
        String uId = ShareUtil.getString(this,"uId","");
        if(TextUtils.isEmpty(uId)){
            getUserId();
            Logger.e("QQ:"+uId);
        }
        //初始化数据
        initData();
        initView();
    }

    //获取Uid,保存账号UID
    private void getUserId(){
        String uUsername =
                ShareUtil.getString(this,StaticClass.USERNAME,"");
        Logger.e("uUsername:"+uUsername);
        String url = StaticClass.LOGIN_API+"?uUsername="+uUsername+"&uPassword="+
                UtilTools.EncoderByMd5("12345");
        Logger.e("loginUrl:"+url);
        getData(url);
       // getData(url);
        //传账号密码
//        HttpParams params = new HttpParams();  params.put("uUsername",uUsername);
//        params.put("uPassword",
//                UtilTools.EncoderByMd5("12345"));
//        Logger.e("uUsername:"+uUsername);
//        RxVolley.get(StaticClass.LOGIN_API, params,new HttpCallback() {
//            @Override
//            public void onSuccess(String t) {
//                //t为json，之后的查询根据uID查询
//                Logger.e("jsonArray:"+t);
//                if (!TextUtils.isEmpty(t)&&!t.equals("fail")) {
//                    //解析json保存UID
//                    parseJson(t);
//                }
//            }
//        });
    }
    private void getData(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {//回调的方法执行在子线程。
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String uId = jsonObject.getString("uId");
                        Logger.e("okhttp:"+uId);
                        ShareUtil.putString(MainActivity.this,"uId",uId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    /*private void parseJson(String t) {
        try {
            Logger.e("jsonArry:"+t);
            JSONObject jsonObject = new JSONObject(t);
            String uId = jsonObject.getString("uId");
            Logger.e("aaaUid:"+uId);
            String uUsername = jsonObject.getString("uUsername");
            ShareUtil.putString(this,"uId",uId);
            ShareUtil.putString(this,StaticClass.USERNAME,uUsername);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/
    //初始化数据
    private void initData() {
        titleList = new ArrayList<>();
        titleList.add(getString(R.string.device));
        titleList.add(getString(R.string.butler));
        titleList.add(getString(R.string.deal));

        fragmentList = new ArrayList<>();
        fragmentList.add(new DeviceFragment());
        fragmentList.add(new ButlerFragment());
        fragmentList.add(new DealFragment());
    }

    private void initView() {
        tabLayout = findViewById(R.id.mTabLayout);
        viewPager = findViewById(R.id.mViewPager);
        fabSetting = findViewById(R.id.fab_setting);
        //默认隐藏
        fabSetting.setVisibility(View.GONE);
        fabSetting.setOnClickListener(this);
        //进行viewpager的预加载
        viewPager.setOffscreenPageLimit(fragmentList.size());
        //滑动监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position!=1) {
                    fabSetting.setVisibility(View.VISIBLE);
                }else{
                    fabSetting.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //设置适配器
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            //选中的item
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }

            //设置标题
            @Override
            public CharSequence getPageTitle(int position) {
                return titleList.get(position);
            }
        });
        //绑定
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_setting:
                startActivity(new Intent(MainActivity.this,
                        SettingActivity.class));
                break;
            default:
        }
    }
}
