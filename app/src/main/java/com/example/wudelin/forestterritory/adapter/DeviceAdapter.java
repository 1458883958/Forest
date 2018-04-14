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

import static com.example.wudelin.forestterritory.adapter.DeviceAdapter.ViewName.ITEM;
import static com.example.wudelin.forestterritory.adapter.DeviceAdapter.ViewName.SWITCH;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.adapter
 * 创建者：   wdl
 * 创建时间： 2018/4/8 19:26
 * 描述：    设备设配器
 */

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> implements View.OnClickListener {
    private List<DeviceData> mList;
    //声明
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    //设置get/set方法
    public OnRecyclerViewItemClickListener getOnRecyclerViewItemClickListener() {
        return onRecyclerViewItemClickListener;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public DeviceAdapter(List<DeviceData> mList) {
        this.mList = mList;
    }

    //创建item
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
//        viewHolder.ivSetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtil.showByStr(parent.getContext(),"ivSetting");
//            }
//        });
//        viewHolder.scAction.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                ToastUtil.showByStr(parent.getContext(),
//                        ""+viewHolder.getAdapterPosition()+isChecked);
//            }
//        });
        return viewHolder;
    }

    //绑定
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DeviceData data = mList.get(position);
        holder.tvName.setText(data.getpName());
        boolean flag = false;
        if (data.getpSwitchstate().equals("off"))
            flag = false;
        else if (data.getpSwitchstate().equals("on"))
            flag = true;
        holder.scAction.setChecked(flag);
        //设置tag
        holder.scAction.setTag(position);
        holder.cardView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        if (onRecyclerViewItemClickListener != null) {
            switch (v.getId()) {
                case R.id.device_switch:
                    onRecyclerViewItemClickListener.onClick(v, SWITCH, position);
                    break;
                case R.id.card_view:
                    onRecyclerViewItemClickListener.onClick(v, ITEM, position);
                    break;
                default:
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        SwitchCompat scAction;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            tvName = cardView.findViewById(R.id.device_name);
            scAction = cardView.findViewById(R.id.device_switch);
            //将创建的View注册点击事件
            scAction.setOnClickListener(DeviceAdapter.this);
            cardView.setOnClickListener(DeviceAdapter.this);
        }
    }

    public enum ViewName {
        SWITCH, ITEM
    }

    //定义接口
    public interface OnRecyclerViewItemClickListener {
        void onClick(View view, ViewName viewName, int position);
    }
}
