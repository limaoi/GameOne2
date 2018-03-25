package com.example.limaoi.gameone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.limaoi.gameone.R;
import com.example.limaoi.gameone.bean.Circle;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Created by limaoi on 2017/10/28.
 * E-mail：autismlm20@vip.qq.com
 */

public class CircleAdapter extends RecyclerView.Adapter<CircleAdapter.ViewHolder> implements View.OnClickListener {

    private ArrayList<Circle> mData;
    private OnItemClickListener mOnItemClickListener = null;

    public CircleAdapter(ArrayList<Circle> Data) {
        this.mData = Data;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void updateData(ArrayList<Circle> Data) {
        this.mData = Data;
        notifyDataSetChanged();
    }

    @Override
    public CircleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_cicle_item, parent, false);
        CircleAdapter.ViewHolder holder = new CircleAdapter.ViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CircleAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        final Circle circle = mData.get(position);
        holder.tv_nickname.setText(circle.getNickname());
        holder.tv_dynamic.setText(circle.getDynamic());
        holder.mDynamicPicture.setImagesData(circle.getDynamicPictureUrl());
        holder.tv_time.setText(circle.getChangeTime());
        if (circle.getUserType().equals("2")) {
            holder.iv_v.setVisibility(View.VISIBLE);
            holder.iv_v.setImageResource(R.mipmap.ic_vimeo2);
        } else if (circle.getUserType().equals("3")) {
            holder.iv_v.setVisibility(View.VISIBLE);
            holder.iv_v.setImageResource(R.mipmap.ic_vimeo3);
        } else if (circle.getUserType().equals("4")) {
            holder.iv_v.setVisibility(View.VISIBLE);
            holder.iv_v.setImageResource(R.mipmap.ic_vimeo4);
        } else if (circle.getUserType().equals("5")) {
            holder.iv_v.setVisibility(View.VISIBLE);
            holder.iv_v.setImageResource(R.mipmap.ic_vimeo5);
        } else if (circle.getUserType().equals("1")) {
            holder.iv_v.setVisibility(View.INVISIBLE);
        }
        Glide.with(getApplicationContext()).load(circle.getHeadPictureUrl()).into(holder.circleImageView_head_photo);
        holder.btn_like.setText(circle.getLikeCount() + "");
        holder.btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btn_like.setText(circle.getLikeCount() + 1 + "");
                Circle circle2 = new Circle();
                circle2.setLikeCount(circle.getLikeCount() + 1);
                circle2.update(circle.getObjectCircleId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e != null) {
                            Toasty.error(getApplicationContext(), "点赞失败", Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
            }
        });
        holder.btn_comment.setText(circle.getCommentCount() + "");
        holder.btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.btn_comment.setText(circle.getCommentCount() + 1 + "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_nickname;
        TextView tv_dynamic;
        TextView tv_time;
        Button btn_like;
        Button btn_comment;
        ImageView iv_v;
        NineGridImageView<String> mDynamicPicture;
        CircleImageView circleImageView_head_photo;

        private NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
            @Override
            protected void onDisplayImage(Context context, ImageView imageView, String s) {
                Glide.with(context).load(s).into(imageView);
            }

            @Override
            protected ImageView generateImageView(Context context) {
                return super.generateImageView(context);
            }

            protected void onItemImageClick(Context context, ImageView imageView, int index, List<String> list) {
                Toast.makeText(context, "image position is " + index, Toast.LENGTH_SHORT).show();
            }

            protected boolean onItemImageLongClick(Context context, ImageView imageView, int index, List<String> list) {
                Toast.makeText(context, "image long click position is " + index, Toast.LENGTH_SHORT).show();
                return true;
            }
        };


        public ViewHolder(View view) {
            super(view);
            tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
            tv_dynamic = (TextView) view.findViewById(R.id.tv_dynamic);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            btn_like = (Button) view.findViewById(R.id.btn_like);
            btn_comment = (Button) view.findViewById(R.id.btn_comment);
            iv_v = (ImageView) view.findViewById(R.id.iv_v);
            mDynamicPicture = (NineGridImageView<String>) view.findViewById(R.id.ngv_dynamicPicture);
            circleImageView_head_photo = (CircleImageView) view.findViewById(R.id.circleImageView_head_photo);
            btn_like.setOnClickListener(this);
            btn_comment.setOnClickListener(this);
            mDynamicPicture.setAdapter(mAdapter);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
