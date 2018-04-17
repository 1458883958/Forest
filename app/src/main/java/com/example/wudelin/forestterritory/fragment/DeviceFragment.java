package com.example.wudelin.forestterritory.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.wudelin.forestterritory.R;
import com.example.wudelin.forestterritory.adapter.DeviceAdapter;
import com.example.wudelin.forestterritory.entity.DeviceData;
import com.example.wudelin.forestterritory.ui.DetialDeviceActivity;
import com.example.wudelin.forestterritory.utils.Logger;
import com.example.wudelin.forestterritory.utils.ShareUtil;
import com.example.wudelin.forestterritory.utils.StaticClass;
import com.example.wudelin.forestterritory.utils.ToastUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import org.json.JSONArray;
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
    public String status = "off";


    @Override
    public void onResume() {
        super.onResume();
        Logger.e("onResume");
        getForService();
    }

    @Override
    public void onStart() {
        super.onStart();
        Logger.e("onStart");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden)
            getForService();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device, null);

        findView(view);
        return view;
    }

    private void findView(View view) {
        btnAddDevice = view.findViewById(R.id.add_device);
        btnAddDevice.setOnClickListener(this);
        recyclerView = view.findViewById(R.id.recycler_device);
        layoutmanager = new LinearLayoutManager(view.getContext());

        //设置RecyclerView 布局
        recyclerView.setLayoutManager(layoutmanager);
        adapter = new DeviceAdapter(mList);
        recyclerView.setAdapter(adapter);
        getForService();
        adapter.setOnRecyclerViewItemClickListener(new DeviceAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, DeviceAdapter.ViewName viewName, int position) {
                //根据viewName区分
                switch (viewName) {
                    case SWITCH:
                        String url = null;
                        final SwitchCompat switchCompat = view.findViewById(R.id.device_switch);
                        Logger.e("switchCompat:" + switchCompat.isChecked());
                        if (switchCompat.isChecked()) {
                            url = StaticClass.ON_PI;
                        } else {
                            url = StaticClass.OFF_PI;
                        }
                        DeviceData data = mList.get(position);
                        Logger.e("pID:" + data.getpId() + "pIpaddress:" + data.getpIpaddress());
                        HttpParams params = new HttpParams();
                        params.put("pId", data.getpId());
                        params.put("pIpaddress", data.getpIpaddress());
                        url += "?pId=" + data.getpId() + "&pIpaddress=" + data.getpIpaddress();
                        Logger.e("url:" + url);
                        getData(url);
                        break;
                    case ITEM:
                        Intent intent = new Intent(getActivity(),
                                DetialDeviceActivity.class);
                        final DeviceData data1 = mList.get(position);
                        intent.putExtra("pId",data1.getpId());
                        intent.putExtra("pIpaddress",data1.getpIpaddress());
                        startActivity(intent);
                        break;
                    default:
                }
            }

            @Override
            public void onItemLongOnClick(View view, int pos) {
                showPopMenu(view,pos);
            }
        });

    }

    public void showPopMenu(View view,final int pos){
        PopupMenu popupMenu = new PopupMenu(getActivity(),view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_item,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                //删除网络存储
                removeService(pos);
                return false;
            }
        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });
        popupMenu.show();
    }

    private void removeService(final int pos) {
        HttpParams params = new HttpParams();
        params.put("pId",mList.get(pos).getpId());
        RxVolley.post(StaticClass.DELETE_RASP, params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                if(!TextUtils.isEmpty(t)&&t.equals("success")){
                    adapter.removeItem(pos);
                }else{
                    ToastUtil.showByStr(getActivity(),getString(R.string.unbind));
                }
            }
        });
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
                    Logger.e(getString(R.string.get_data_success));
                    Logger.e(response.body().string());
                }
            }
        });
    }

    //获取已绑定设备
    private void getForService() {
        String uId = ShareUtil.getString(getActivity(),"uId","");
        //String pIdadress = ShareUtil.getString(getActivity(), "pIpaddress", "");
        if (uId==null) {
            return;
        } else {
            String a = ShareUtil.getString(getActivity(), "uId", "");
            Logger.e("uid"+uId);
            HttpParams params = new HttpParams();
            params.put("uId", a);
            //RxVolley.Builder builder = new RxVolley.Builder().shouldCache(false)
            //请求
            RxVolley.post(StaticClass.LIST_PI, params, new HttpCallback() {
                @Override
                public void onSuccess(String t) {
                    if (!TextUtils.isEmpty(t) && !t.equals("fail")) {
                        Logger.e(t);
                        parseJson(t);
                    } else {
                        new AlertDialog.Builder(getActivity())
                                .setTitle(getString(R.string.note))
                                .setMessage(getString(R.string.check_device_binding))
                                .setNegativeButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create().show();
                    }
                }
            });
        }
    }

    private void parseJson(String t) {
        try {
            if(mList.size()>0) {
                mList.clear();
                adapter.notifyDataSetChanged();
            }
            Logger.e("GET:"+t);
            JSONArray jsonArray = new JSONArray(t);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                DeviceData data = new DeviceData();
                data.setpIpaddress(object.getString("pIpaddress"));
                data.setpName(object.getString("pName"));
                data.setpRemark(object.getString("pRemark"));
                data.setpDelayTime(Integer.parseInt(object.getString("pDelayed")));
                data.setpThreshold(Double.parseDouble(object.getString("pThreshold")));
                data.setpSwitchstate(object.getString("pSwitchstate"));
                data.setpBootsate(object.getString("pBootstate"));
                Logger.e(""+object.getString("pId"));
                data.setpId(object.getString("pId"));
                mList.add(data);
            }
            adapter.notifyItemInserted(mList.size()-1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_device:
                checkPermission();
                break;
            default:
        }
    }

    private void startScan() {
        //打开扫描界面扫描条形码或二维码
        Intent openCameraIntent = new Intent(getActivity(),
                CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);
    }

    //检测相机权限
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CODE);
        } else {
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
                .inflate(R.layout.pi_edit_dialog, null);
        MaterialEditText pIpaddress = view.findViewById(R.id.pi_ipaddress);
        pIpaddress.setText(scanResult);
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.confirmation_information))
                .setView(view)
                .setPositiveButton(getString(R.string.binding), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MaterialEditText pName = view.findViewById(R.id.pi_name);
                        MaterialEditText pRemark = view.findViewById(R.id.pi_remark);
                        MaterialEditText pThreshold = view.findViewById(R.id.pi_threshold);
                        MaterialEditText pDelaytime = view.findViewById(R.id.pi_delaytime);
                        MaterialEditText pIpaddress = view.findViewById(R.id.pi_ipaddress);
                        //不可编辑
                        ToastUtil.showByStr(getActivity(), "result:" + scanResult);

                        String piName = pName.getText().toString().trim();
                        String piRemark = pRemark.getText().toString().trim();
                        String piThreshold = pThreshold.getText().toString().trim();
                        String piDelaytime = pDelaytime.getText().toString().trim();
                        String piIpaddress = pIpaddress.getText().toString().trim();

                        if (!TextUtils.isEmpty(piName)
                                && !TextUtils.isEmpty(piThreshold) && !TextUtils.isEmpty(piRemark)
                                && !TextUtils.isEmpty(piDelaytime)) {
                            DeviceData data1 = new DeviceData();
                            data1.setpIpaddress(piIpaddress);
                            data1.setpName(piName);
                            data1.setpRemark(piRemark);
                            data1.setpDelayTime(Integer.parseInt(piDelaytime));
                            data1.setpBootsate("off");
                            data1.setpThreshold(Double.parseDouble(piThreshold));
                            data1.setpSwitchstate("off");
                            //成功绑定的基础是账户与树莓派信息关联（即在服务器中成功插入相应的库）
                            //绑定成功后在服务器上保存
                            sendToService(data1);

                        }

                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //自动消失
            }
        }).setCancelable(false).create().show();
    }

    //保存服务器
    private void sendToService(final DeviceData data) {
        HttpParams params = new HttpParams();
        params.put("uId", ShareUtil.getString(getActivity(), "uId", ""));
        String a = ShareUtil.getString(getActivity(), "uId", "");
        Logger.e("auId:" + a);
        params.put("pIpaddress", data.getpIpaddress());
        params.put("pName", data.getpName());
        params.put("pRemark", data.getpRemark());
        params.put("pThreshold", "" + data.getpThreshold());
        params.put("pSwitchstate", data.getpSwitchstate());
        params.put("pDelayed", data.getpDelayTime());
        params.put("pBootstate", data.getpBootsate());
        RxVolley.post(StaticClass.SAVE_PI_API, params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                if (t != null) {
                    data.setpId(t);
                    ShareUtil.putString(getActivity(), "pIpaddress", data.getpIpaddress());
                    mList.add(data);
                    adapter.notifyItemInserted(mList.size()-1);
                } else {
                    ToastUtil.showByStr(getActivity(), getString(R.string.binding_failed));
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
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startScan();
                } else {
                    ToastUtil.showByStr(getActivity(), "permission denied！");
                }
                break;
            default:
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
