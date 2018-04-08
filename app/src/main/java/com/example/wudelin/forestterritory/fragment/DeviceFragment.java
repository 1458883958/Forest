package com.example.wudelin.forestterritory.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.wudelin.forestterritory.R;
import com.example.wudelin.forestterritory.adapter.DeviceAdapter;
import com.example.wudelin.forestterritory.entity.DeviceData;
import com.example.wudelin.forestterritory.utils.ToastUtil;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.fragment
 * 创建者：   wdl
 * 创建时间： 2018/4/7 21:08
 * 描述：    我的设备
 */

public class DeviceFragment extends Fragment implements View.OnClickListener {
    private Button btnAddDevice;
    private RecyclerView recyclerView;
    private DeviceAdapter adapter;
    private List<DeviceData> mList = new ArrayList<>();
    private static final int PERMISSION_REQUEST_CODE = 100;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device,null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        btnAddDevice = view.findViewById(R.id.add_device);
        btnAddDevice.setOnClickListener(this);
        recyclerView = view.findViewById(R.id.recycler_device);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(view.getContext());
        //设置RecyclerView 布局
        recyclerView.setLayoutManager(layoutmanager);
        adapter = new DeviceAdapter(mList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_device:
                checkPermission();
                break;
            default:
        }
    }

    private void startScan(){
        //打开扫描界面扫描条形码或二维码
        Intent openCameraIntent = new Intent(getActivity(),
                CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);
    }
    //检测相机权限
    private void checkPermission() {
        if(ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CODE);
        }else {
            startScan();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //扫描二维码的结果
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            DeviceData data1 = new DeviceData();
            data1.setName(scanResult);
            mList.add(data1);
            adapter.notifyItemInserted(0);
            ToastUtil.showByStr(getActivity(),scanResult);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                if(grantResults.length>0&&
                        grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    startScan();
                }else{
                    ToastUtil.showByStr(getActivity(),"permission denied！");
                }
                break;
            default:
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
