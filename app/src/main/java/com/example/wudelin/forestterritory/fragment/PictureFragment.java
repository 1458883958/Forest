package com.example.wudelin.forestterritory.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.wudelin.forestterritory.R;
import com.example.wudelin.forestterritory.adapter.PictureAdapter;
import com.example.wudelin.forestterritory.entity.Picture;
import com.example.wudelin.forestterritory.utils.Logger;
import com.example.wudelin.forestterritory.utils.PicassoUtil;
import com.example.wudelin.forestterritory.utils.ShareUtil;
import com.example.wudelin.forestterritory.utils.StaticClass;
import com.example.wudelin.forestterritory.utils.ToastUtil;
import com.example.wudelin.forestterritory.view.CustomDialog;
import com.github.chrisbanes.photoview.PhotoView;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.client.ProgressListener;
import com.kymjs.rxvolley.http.VolleyError;
import com.kymjs.rxvolley.toolbox.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.fragment
 * 创建者：   wdl
 * 创建时间： 2018/4/7 21:13
 * 描述：    历史图片
 */

public class PictureFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {
    //刷新
    private SwipeRefreshLayout refreshLayout;
    private GridView gridView;
    private PictureAdapter adapter;
    private List<Picture> mList = new ArrayList<>();
    //预览图片
    private CustomDialog dialog;
    private PhotoView photoView;
    //下载图片按钮
    private ImageButton imageButton;
    //图片URL
    private String url;
    //文件保存路径
    private String path;
    //文件名
    private String fileName;
    private File file;
    //下载状态
    private static final int HANDLE_DOWNLOADING = 100;
    private static final int HANDLE_SUCCESS = 101;
    private static final int HANDLE_FAIL = 102;
    private Handler mHandler = new Handler();
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLE_DOWNLOADING:
                    break;
                case HANDLE_SUCCESS:
                    Logger.e("下载完成...");
                    // 第二步：其次把文件插入到系统图库
                    try {
                        MediaStore.
                                Images.
                                Media.insertImage(getActivity().getContentResolver(),
                                path, fileName, null);
                        //   /storage/emulated/0/Boohee/1493711988333.jpg
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    // 第三步：最后通知图库更新
                    getActivity()
                            .sendBroadcast(new Intent(
                                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                    Uri.parse("file://" + file)));
                    break;
                case HANDLE_FAIL:
                    Logger.e("下载失败...");
                    break;
                default:
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture,null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        gridView = view.findViewById(R.id.gv_picture);
        //初始化预览弹窗
        dialog = new CustomDialog(getActivity(),
                LinearLayout.LayoutParams.MATCH_PARENT
                ,LinearLayout.LayoutParams.MATCH_PARENT,
                R.layout.his_photo,R.style.Theme_dialog,
                Gravity.CENTER,R.style.pop_anim_style);
        photoView = dialog.findViewById(R.id.photo_view);
        imageButton = dialog.findViewById(R.id.ib_download);
        imageButton.setOnClickListener(this);
        refreshLayout = view.findViewById(R.id.refresh);

        photoView.setOnLongClickListener(this);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //加载图片
                PicassoUtil.loadDefault(getActivity(),
                        mList.get(position).getIvUrl(),photoView);
                url = mList.get(position).getIvUrl();
                dialog.show();
            }
        });

        getPicture();
    }

    //获取图片
    private void getPicture() {
        mList.clear();
        HttpParams params = new HttpParams();

        //params.put("pIpaddress", ShareUtil.getString(getActivity(),"pIpaddress",""));
        params.put("pIpaddress","218.5.241.6");
        RxVolley.get(StaticClass.HIS_PICTURE_API,params,new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Logger.d("pic"+t);
                if(!TextUtils.isEmpty(t)) {
                    //数据处理
                    parseResponse(t);
                }
                
                //测试
                //parseJson(t);
                
            }
            @Override
            public void onFailure(VolleyError error) {
                Logger.d(error.getMessage());
                ToastUtil.showByStr(getActivity(),""+error.getMessage());
            }
        });
    }

    private void parseJson(String t) {

        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i <jsonArray.length() ; i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                String url = json.getString("url");
                Picture picture = new Picture(url,i+"");
                mList.add(picture);
                adapter = new PictureAdapter(mList,getActivity());
                gridView.setAdapter(adapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void parseResponse(String t) {
        String mUrl[] = t.split(" ");
        String time[] = t.split(" ");
        String sortT[] = new String[time.length];
        for(int i = 0;i<mUrl.length;i++){
            sortT[i] = time[i].replace(".jpg","");
            mUrl[i] = StaticClass.ALY_IP+"/photo/"+"218.5.241.6"+"/"+mUrl[i];
        }
        for (int i = 0; i < mUrl.length; i++) {
            Picture picture = new Picture(mUrl[i],sortT[i]);
            mList.add(picture);
        }
        adapter = new PictureAdapter(mList,getActivity());
        gridView.setAdapter(adapter);
    }

    //下载图片
    private void downLoad() {
        // 第一步：首先保存图片
        //将Bitmap保存图片到指定的路径/sdcard/Boohee/下，文件名以当前系统时间命名,但是这种方法保存的图片没有加入到系统图库中
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");

        if (!appDir.exists()) {
            appDir.mkdir();
        }
        fileName = System.currentTimeMillis() + ".jpg";
        file = new File(appDir, fileName);
        path = file.getAbsolutePath();
        Logger.e(fileName+"\n"+file.getAbsolutePath()+"\n");
        RxVolley.download(path, url, new ProgressListener() {
            @Override
            public void onProgress(long transferredBytes, long totalSize) {
                handler.sendEmptyMessage(HANDLE_DOWNLOADING);
            }
        }, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                handler.sendEmptyMessage(HANDLE_SUCCESS);
            }

            @Override
            public void onFailure(VolleyError error) {
                handler.sendEmptyMessage(HANDLE_FAIL);
            }
        });
    }

    //长按下载
    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.photo_view:
                ToastUtil.showByStr(getActivity(),"长按下载");
                check();
                break;
            default:
        }
        return false;
    }
    //权限检查
    private void check() {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }else {
            downLoad();
        }
    }

    //下载按钮
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_download:
                ToastUtil.showByStr(getActivity(),url);
                check();
                break;
            default:
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0&&grantResults[0]!=PackageManager.PERMISSION_GRANTED){
            ToastUtil.showByStr(getActivity(),"无权限无法正常于使用");
        }else {
            downLoad();
        }
    }
}
