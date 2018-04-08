package com.example.wudelin.forestterritory.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.wudelin.forestterritory.R;
import com.example.wudelin.forestterritory.adapter.PictureAdapter;
import com.example.wudelin.forestterritory.entity.Picture;
import com.example.wudelin.forestterritory.utils.Logger;
import com.example.wudelin.forestterritory.utils.StaticClass;
import com.example.wudelin.forestterritory.utils.ToastUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.fragment
 * 创建者：   wdl
 * 创建时间： 2018/4/7 21:13
 * 描述：    历史图片
 */

public class PictureFragment extends Fragment{
    private SwipeRefreshLayout refreshLayout;
    private GridView gridView;
    private PictureAdapter adapter;
    private List<Picture> mList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture,null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        refreshLayout = view.findViewById(R.id.refresh);
        gridView = view.findViewById(R.id.gv_picture);
        getPicture();
    }

    //获取图片
    private void getPicture() {
        RxVolley.get(StaticClass.HIS_PICTURE_API, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Logger.d(t);
                //数据处理
                parseResponse(t);
            }
            @Override
            public void onFailure(VolleyError error) {
                Logger.d(error.getMessage());
                ToastUtil.showByStr(getActivity(),""+error.getMessage());
            }
        });
    }
    private void parseResponse(String t) {
        String mUrl[] = t.split(" ");
        String time[] = t.split(" ");
        String sortT[] = new String[time.length];
        for(int i = 0;i<mUrl.length;i++){
            sortT[i] = time[i].replace(".jpg","");
            mUrl[i] = StaticClass.ALY_IP+mUrl[i];
        }
        for (int i = 0; i < mUrl.length; i++) {
            Picture picture = new Picture(mUrl[i],sortT[i]);
            mList.add(picture);
        }
        adapter = new PictureAdapter(mList,getActivity());
        gridView.setAdapter(adapter);
    }
}
