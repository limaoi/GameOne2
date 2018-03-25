package com.example.limaoi.gameone.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.limaoi.gameone.R;
import com.example.limaoi.gameone.bean.Info;

import java.util.ArrayList;

/**
 * Created by limaoi on 2017/7/6.
 * E-mail：autismlm20@vip.qq.com
 */

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {

    private ArrayList<Info> mData;

    public InfoAdapter(ArrayList<Info> Data) {
        this.mData = Data;
    }

    public void updateData(ArrayList<Info> Data) {
        this.mData = Data;
        notifyDataSetChanged();
    }

    @Override
    public InfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_info_item, parent, false);
        InfoAdapter.ViewHolder holder = new InfoAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Info info = mData.get(position);
        holder.tv_info_name.setText(info.getTitle());
        holder.tv_value.setText(info.getValue());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_info_name;
        TextView tv_value;

        public ViewHolder(View view) {
            super(view);
            tv_info_name = (TextView) view.findViewById(R.id.tv_info_name);
            tv_value = (TextView) view.findViewById(R.id.tv_value);
        }
    }
}
