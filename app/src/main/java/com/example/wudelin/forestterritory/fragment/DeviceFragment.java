package com.example.wudelin.forestterritory.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.wudelin.forestterritory.R;
import com.example.wudelin.forestterritory.adapter.DeviceAdapter;
import com.example.wudelin.forestterritory.entity.DeviceData;
import com.example.wudelin.forestterritory.utils.Logger;
import com.example.wudelin.forestterritory.utils.StaticClass;
import com.example.wudelin.forestterritory.utils.ToastUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
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
   private LinearLayoutManager layoutmanager;
    private static final int PERMISSION_REQUEST_CODE = 100;
    public String status="off";
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
        layoutmanager = new LinearLayoutManager(view.getContext());
        //获取网络绑定资源
        getForService();
        //设置RecyclerView 布局
        recyclerView.setLayoutManager(layoutmanager);
        adapter = new DeviceAdapter(mList);
        recyclerView.setAdapter(adapter);
        adapter.setOnRecyclerViewItemClickListener(new DeviceAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, DeviceAdapter.ViewName viewName, int position) {
                //根据viewName区分
                switch (viewName){
                    case SWITCH:
                        final DeviceData data = mList.get(position);
                        SwitchCompat switchCompat = view.findViewById(R.id.device_switch);
                        boolean flag = switchCompat.isChecked()?true:false;
                        status = flag?"on":"off";
                        Logger.e(""+flag+"   "+status+"\n"+data.getpIpaddress());
                        data.setpSwitchstate(status);
                        //控制树莓派状态
                        new Thread(new Runnable(){
                            @Override
                            public void run() {
                                // TODO: http request.
                                Socket socket;
                                try {
                                    Logger.e(data.getpIpaddress()+"  ");
                                    socket = new Socket(data.getpIpaddress(), 17470);
                                    OutputStream ops = socket.getOutputStream();
                                    OutputStreamWriter opsw = new OutputStreamWriter(ops);
                                    BufferedWriter bw = new BufferedWriter(opsw);
                                    bw.write(status);
                                    bw.flush();
                                    socket.close();
                                } catch (UnknownHostException e) {
                                    // TODO 自动生成的 catch 块
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    // TODO 自动生成的 catch 块
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        //每更改一次，更新一次服务器的pi状态

                        break;
                    case ITEM:
                        ToastUtil.showByStr(getActivity(),"我是item"+position);
                        break;
                }
            }
        });
    }

    private void getForService() {
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

    //数据回传
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //扫描二维码的结果
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");

            showWaiterAuthorizationDialog(scanResult);
        }
    }

    //对话框 确定绑定
    private void showWaiterAuthorizationDialog(final String scanResult) {

        final View view = LayoutInflater
                .from(getActivity())
                .inflate(R.layout.pi_edit_dialog,null);
        MaterialEditText pIpaddress = view.findViewById(R.id.pi_ipaddress);
        pIpaddress.setText(scanResult);
        new AlertDialog.Builder(getActivity())
                .setTitle("确认信息")
                .setView(view)
                .setPositiveButton("绑定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MaterialEditText pName = view.findViewById(R.id.pi_name);
                        MaterialEditText pRemark = view.findViewById(R.id.pi_remark);
                        MaterialEditText pThreshold = view.findViewById(R.id.pi_threshold);
                        MaterialEditText pDelaytime = view.findViewById(R.id.pi_delaytime);
                        MaterialEditText pIpaddress = view.findViewById(R.id.pi_ipaddress);
                        //不可编辑
                        ToastUtil.showByStr(getActivity(),"result:"+scanResult);

                        String piName = pName.getText().toString().trim();
                        String piRemark = pRemark.getText().toString().trim();
                        String piThreshold = pThreshold.getText().toString().trim();
                        String piDelaytime = pDelaytime.getText().toString().trim();
                        String piIpaddress = pIpaddress.getText().toString().trim();

                        if(!TextUtils.isEmpty(piName)
                                &&!TextUtils.isEmpty(piThreshold)&&!TextUtils.isEmpty(piRemark)
                                &&!TextUtils.isEmpty(piDelaytime)){
                            DeviceData data1= new DeviceData();
                            data1.setpIpaddress(piIpaddress);
                            data1.setpName(piName);
                            data1.setpRemark(piRemark);
                            data1.setpDelayTime(Integer.parseInt(piDelaytime));
                            data1.setpBootsate("off");
                            data1.setpThreshold(Double.parseDouble(piThreshold));
                            data1.setpSwitchstate("off");
                            //成功绑定的基础是账户与树莓派信息关联（即在服务器中成功插入相应的库）
                            //绑定成功后在服务器上保存
                            //sendToService(data1);
                            mList.add(data1);
                            adapter.notifyItemInserted(mList.size());

                        }

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    //自动消失
            }
        }) .setCancelable(false).create().show();
    }

    //保存服务器
    private void sendToService(DeviceData data) {
        HttpParams params = new HttpParams();
        params.put("pIpaddress",data.getpIpaddress());
        params.put("pName",data.getpName());
        params.put("pRemark",data.getpRemark());
        params.put("pThreshold",""+data.getpThreshold());
        params.put("pSwitchstate",data.getpSwitchstate());
        params.put("pBootstate",data.getpBootsate());
        RxVolley.post(StaticClass.SAVE_PI_API, params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                if(t.equals("success")){

                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
            }
        });
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
