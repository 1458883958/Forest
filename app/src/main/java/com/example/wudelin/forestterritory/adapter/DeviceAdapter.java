package com.example.wudelin.forestterritory.adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wudelin.forestterritory.R;
import com.example.wudelin.forestterritory.entity.DeviceData;
import com.example.wudelin.forestterritory.utils.ToastUtil;

import java.util.List;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.adapter
 * 创建者：   wdl
 * 创建时间： 2018/4/8 19:26
 * 描述：    设备设配器
 */

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder>{
    private List<DeviceData> mList;

    public DeviceAdapter(List<DeviceData> mList) {
        this.mList = mList;
    }

    //创建item
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recycler_item,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showByStr(parent.getContext(),"ivSetting");
            }
        });
        viewHolder.scAction.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ToastUtil.showByStr(parent.getContext(),
                        ""+viewHolder.getAdapterPosition()+isChecked);
            }
        });
        return viewHolder;
    }

    //绑定
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DeviceData data = mList.get(position);
        holder.tvName.setText(data.getName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivSetting;
        SwitchCompat scAction;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            tvName = cardView.findViewById(R.id.device_name);
            scAction = cardView.findViewById(R.id.device_switch);
            ivSetting = cardView.findViewById(R.id.device_setting);
        }
    }
}
