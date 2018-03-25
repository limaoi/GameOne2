package com.example.limaoi.gameone.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.limaoi.gameone.R;
import com.example.limaoi.gameone.adapter.VideoItemAdapter;
import com.example.limaoi.gameone.bean.Video;
import com.example.limaoi.gameone.utils.TimeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;
import cn.jzvd.JZVideoPlayerStandard;
import es.dmoral.toasty.Toasty;

/**
 * Created by limaoi on 2017/12/11.
 * E-mail：autismlm20@vip.qq.com
 */

public class LOLVideoFragment extends BaseFragment implements View.OnClickListener {

    public static String TABLAYOUT_FRAGMENT = "LOLVideoFragment";
    private PtrClassicFrameLayout mPtrLayout;
    private ArrayList<Video> videoList = new ArrayList<>();
    private RecyclerViewFinal mRecyclerViewFinal;
    private VideoItemAdapter mVideoItemAdapter;
    private JZVideoPlayerStandard jzVideoPlayer;
    private int mPage = 1;

    public static LOLVideoFragment newInstance(int type) {
        LOLVideoFragment fragment = new LOLVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TABLAYOUT_FRAGMENT, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lol_video, container, false);

        //初始化Bmob
        Bmob.initialize(getActivity(), "a21a5524eff971e709218fdd5420bec2");

        initViews(view);

        initEvents();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        jzVideoPlayer.releaseAllVideos();
    }

    private void initViews(View view) {
        mPtrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_rv_layout);
        mRecyclerViewFinal = (RecyclerViewFinal) view.findViewById(R.id.rv_RecyclerView);
        jzVideoPlayer = (JZVideoPlayerStandard)view.findViewById(R.id.videoplayer);
    }

    private void initEvents() {
        if (isNetworkConnected(getActivity())) {
            loadingData();
            mPtrLayout.autoRefresh();
        } else {
            mRecyclerViewFinal.setHasLoadMore(false);
            Toasty.error(getActivity(), "请检查网络", Toast.LENGTH_SHORT, true).show();
        }

        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewFinal.setLayoutManager(LinearLayoutManager);
        mVideoItemAdapter = new VideoItemAdapter(videoList);
        mRecyclerViewFinal.setAdapter(mVideoItemAdapter);
        mVideoItemAdapter.setOnItemClickListener(new VideoItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        setSwipeRefreshInfo();
    }

    private void setSwipeRefreshInfo() {
        mPtrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //发起下拉刷新请求
                requestData(1);
            }
        });
        mPtrLayout.setLastUpdateTimeRelateObject(this);
        mRecyclerViewFinal.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                //发起加载更多请求
                requestData(mPage + 1);
            }
        });
    }

    private void loadingData() {
        videoList.clear();
        BmobQuery<Video> query = new BmobQuery<Video>();
        query.setLimit(5);
        query.addWhereEqualTo("label", "英雄联盟");
        query.order("-createdAt");
        query.findObjects(new FindListener<Video>() {
            @Override
            public void done(List<Video> list, BmobException e) {
                if (e == null) {
                    String time = null;
                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");
                    if (list.size() < 5) {
                        mRecyclerViewFinal.setHasLoadMore(false);
                        for (int i = 0; i < list.size(); i++) {
                            try {
                                time = TimeUtil.getTimeFormatText(formatter.parse(list.get(i).getCreatedAt()));
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                            Video video = new Video(list.get(i).getTitle(), list.get(i).getVideoUrl(),
                                    list.get(i).getVideoPicUrl(), list.get(i).getNickname(),
                                    list.get(i).getHeadPictureUrl(), 0, 0,list.get(i).getUserType());
                            videoList.add(video);
                        }
                    } else {
                        mRecyclerViewFinal.setHasLoadMore(true);
                        for (int i = 0; i < list.size(); i++) {
                            try {
                                time = TimeUtil.getTimeFormatText(formatter.parse(list.get(i).getCreatedAt()));
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                            Video video = new Video(list.get(i).getTitle(), list.get(i).getVideoUrl(),
                                    list.get(i).getVideoPicUrl(), list.get(i).getNickname(),
                                    list.get(i).getHeadPictureUrl(), 0, 0,list.get(i).getUserType());
                            videoList.add(video);
                        }
                    }
                }
            }
        });
    }

    private void requestData(int page) {
        jzVideoPlayer.releaseAllVideos();
        if (page == 1) {
            loadingData();
            mPage = 1;
            mPtrLayout.onRefreshComplete();//完成下拉刷新
        } else {
            BmobQuery<Video> query = new BmobQuery<Video>();
            query.setLimit(5);
            query.setSkip(5 * mPage);
            query.addWhereEqualTo("label", "英雄联盟");
            query.order("-createdAt");
            query.findObjects(new FindListener<Video>() {
                @Override
                public void done(List<Video> list, BmobException e) {
                    if (e == null) {
                        String time = null;
                        SimpleDateFormat formatter = new SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss");
                        if (list.size() < 5) {
                            mRecyclerViewFinal.setHasLoadMore(false);
                            for (int i = 0; i < list.size(); i++) {
                                try {
                                    time = TimeUtil.getTimeFormatText(formatter.parse(list.get(i).getCreatedAt()));
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
                                Video video = new Video(list.get(i).getTitle(), list.get(i).getVideoUrl(),
                                        list.get(i).getVideoPicUrl(), list.get(i).getNickname(),
                                        list.get(i).getHeadPictureUrl(), 0, 0,list.get(i).getUserType());
                                videoList.add(video);
                            }
                        } else {
                            mRecyclerViewFinal.setHasLoadMore(true);
                            for (int i = 0; i < list.size(); i++) {
                                try {
                                    time = TimeUtil.getTimeFormatText(formatter.parse(list.get(i).getCreatedAt()));
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
                                Video video = new Video(list.get(i).getTitle(), list.get(i).getVideoUrl(),
                                        list.get(i).getVideoPicUrl(), list.get(i).getNickname(),
                                        list.get(i).getHeadPictureUrl(), 0, 0,list.get(i).getUserType());
                                videoList.add(video);
                            }
                        }
                    }
                }
            });
            mPage++;
            mRecyclerViewFinal.onLoadMoreComplete();//完成加载更多
        }

        mVideoItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
