package com.example.wudelin.forestterritory.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wudelin.forestterritory.R;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.fragment
 * 创建者：   wdl
 * 创建时间： 2018/4/7 21:14
 * 描述：    处理图片
 */

public class DealFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deal,null);
        return view;
    }
}
