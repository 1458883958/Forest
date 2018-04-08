package com.example.wudelin.forestterritory.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wudelin.forestterritory.R;
import com.example.wudelin.forestterritory.entity.Picture;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.adapter
 * 创建者：   wdl
 * 创建时间： 2018/4/8 20:06
 * 描述：    历史图片适配器
 */

public class PictureAdapter extends BaseAdapter{
    private List<Picture> mList;
    private LayoutInflater layoutInflater;
    private Context mContext;

    public PictureAdapter(List<Picture> mList,Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
        this.layoutInflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        //判断缓存
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.gv_item,null);
            viewHolder.photoView = convertView.findViewById(R.id.photo_view);
            viewHolder.tvTime = convertView.findViewById(R.id.tm_picture);
            //设置缓存
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //赋值
        Picture picture = mList.get(position);
        viewHolder.tvTime.setText(picture.getTime());
        Glide.with(mContext).load(picture.getIvUrl())
                .placeholder(R.drawable.ic_launcher)
                .into(viewHolder.photoView);
        return convertView;
    }
    private class ViewHolder{
        private PhotoView photoView;
        private TextView tvTime;
    }
}
