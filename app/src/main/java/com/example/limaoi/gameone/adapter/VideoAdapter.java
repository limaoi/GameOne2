package com.example.limaoi.gameone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.limaoi.gameone.R;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.impl.OnItemClickListener;
import com.yanzhenjie.album.util.AlbumUtils;

import java.util.List;

/**
 * Created by limaoi on 2017/12/15.
 * E-mail：autismlm20@vip.qq.com
 */

public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private LayoutInflater mInflater;
    private int itemSize;
    private OnItemClickListener mItemClickListener;
    private List<AlbumFile> mAlbumFiles;

    public VideoAdapter(Context context, int itemSize, OnItemClickListener itemClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.itemSize = itemSize;
        this.mItemClickListener = itemClickListener;
    }

    public void notifyDataSetChanged(List<AlbumFile> videoPathList) {
        this.mAlbumFiles = videoPathList;
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        AlbumFile albumFile = mAlbumFiles.get(position);
        if (albumFile.getMediaType() == AlbumFile.TYPE_IMAGE) {
            return AlbumFile.TYPE_IMAGE;
        } else {
            return AlbumFile.TYPE_VIDEO;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case AlbumFile.TYPE_VIDEO: {
                return new VideoViewHolder(mInflater.inflate(R.layout.item_content_video, parent, false), itemSize, mItemClickListener);
            }
            default: {
                return new VideoViewHolder(mInflater.inflate(R.layout.item_content_video, parent, false), itemSize, mItemClickListener);
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case AlbumFile.TYPE_VIDEO: {
                ((VideoViewHolder) holder).setData(mAlbumFiles.get(position));
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mAlbumFiles == null ? 0 : mAlbumFiles.size();
    }

    private static class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final int itemSize;
        private final OnItemClickListener mItemClickListener;

        private ImageView mIvImage;
        private TextView mTvDuration;

        VideoViewHolder(View itemView, int itemSize, OnItemClickListener itemClickListener) {
            super(itemView);
            itemView.getLayoutParams().height = itemSize;

            this.itemSize = itemSize;
            this.mItemClickListener = itemClickListener;

            mIvImage = (ImageView) itemView.findViewById(R.id.iv_album_content_image);
            mTvDuration = (TextView) itemView.findViewById(R.id.tv_duration);

            itemView.setOnClickListener(this);
        }

        void setData(AlbumFile albumFile) {
            Album.getAlbumConfig().
                    getAlbumLoader().
                    loadAlbumFile(mIvImage, albumFile, itemSize, itemSize);
            mTvDuration.setText(AlbumUtils.convertDuration(albumFile.getDuration()));
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
