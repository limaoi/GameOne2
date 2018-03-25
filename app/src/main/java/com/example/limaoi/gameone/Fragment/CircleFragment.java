package com.example.limaoi.gameone.Fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.limaoi.gameone.EditMsgActivity;
import com.example.limaoi.gameone.R;
import com.example.limaoi.gameone.adapter.CircleAdapter;
import com.example.limaoi.gameone.bean.Circle;
import com.example.limaoi.gameone.utils.TimeUtil;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;
import es.dmoral.toasty.Toasty;


/**
 * Created by limaoi on 2017/6/18.
 * E-mail：autismlm20@vip.qq.com
 */

public class CircleFragment extends BaseFragment implements View.OnClickListener {

    private PtrClassicFrameLayout mPtrLayout;
    private RecyclerViewFinal mRecyclerViewFinal;
    private ArrayList<Circle> circleList = new ArrayList<>();
    private CircleAdapter mCircleAdapter;
    private int mPage = 1;

    @Override
    public void onStart() {
        super.onStart();
        BmobUser bmobUser = BmobUser.getCurrentUser();
        if (bmobUser != null) {
        } else {
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_circle, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //初始化Bmob
        Bmob.initialize(getActivity(), "a21a5524eff971e709218fdd5420bec2");

        MIUISetStatusBarLightMode(getActivity().getWindow(), true);

        initViews(view);

        initEvens();

        return view;
    }

    private void initViews(View view) {
        mPtrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_rv_layout);
        mRecyclerViewFinal = (RecyclerViewFinal) view.findViewById(R.id.rv_games);
    }

    private void initEvens() {
        if (isNetworkConnected(getActivity())) {
            loadingData();
            mPtrLayout.autoRefresh();
        } else {
            mRecyclerViewFinal.setHasLoadMore(false);
            Toasty.error(getActivity(), "请检查网络", Toast.LENGTH_SHORT, true).show();
        }

        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewFinal.setLayoutManager(LinearLayoutManager);
        mCircleAdapter = new CircleAdapter(circleList);
        mRecyclerViewFinal.setAdapter(mCircleAdapter);
        mCircleAdapter.setOnItemClickListener(new CircleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        setSwipeRefreshInfo();
    }

    private void loadingData() {
        circleList.clear();
        BmobQuery<Circle> query = new BmobQuery<Circle>();
        query.setLimit(5);
        query.order("-createdAt");
        query.findObjects(new FindListener<Circle>() {
            @Override
            public void done(List<Circle> list, BmobException e) {
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
                            Circle circle = new Circle(list.get(i).getObjectId(), list.get(i).getHeadPictureUrl(), list.get(i).getNickname(), list.get(i).getDynamic(), list.get(i).getDynamicPictureUrl(), time, list.get(i).getLikeCount(), list.get(i).getCommentCount(), list.get(i).getUserType());
                            circleList.add(circle);
                        }
                    } else {
                        mRecyclerViewFinal.setHasLoadMore(true);
                        for (int i = 0; i < list.size(); i++) {
                            try {
                                time = TimeUtil.getTimeFormatText(formatter.parse(list.get(i).getCreatedAt()));
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                            Circle circle = new Circle(list.get(i).getObjectId(), list.get(i).getHeadPictureUrl(),
                                    list.get(i).getNickname(), list.get(i).getDynamic(), list.get(i).getDynamicPictureUrl(), time,
                                    list.get(i).getLikeCount(), list.get(i).getCommentCount(), list.get(i).getUserType());
                            circleList.add(circle);
                        }
                    }
                }
            }
        });
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
        /*mRecyclerViewFinal.showFailUI();*/
    }

    private void requestData(int page) {
        if (page == 1) { //page == 1表示下拉下拉
            loadingData();
            mPage = 1;
            mPtrLayout.onRefreshComplete();//完成下拉刷新
        } else {
            BmobQuery<Circle> query = new BmobQuery<Circle>();
            query.setLimit(5);
            query.setSkip(5 * mPage);
            query.order("-createdAt");
            query.findObjects(new FindListener<Circle>() {
                @Override
                public void done(List<Circle> list, BmobException e) {
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
                                Circle circle = new Circle(list.get(i).getObjectId(), list.get(i).getHeadPictureUrl(),
                                        list.get(i).getNickname(), list.get(i).getDynamic(),
                                        list.get(i).getDynamicPictureUrl(), time,
                                        list.get(i).getLikeCount(), list.get(i).getCommentCount(), list.get(i).getUserType());
                                circleList.add(circle);
                            }
                        } else {
                            mRecyclerViewFinal.setHasLoadMore(true);
                            for (int i = 0; i < list.size(); i++) {
                                try {
                                    time = TimeUtil.getTimeFormatText(formatter.parse(list.get(i).getCreatedAt()));
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
                                Circle circle = new Circle(list.get(i).getObjectId(), list.get(i).getHeadPictureUrl(),
                                        list.get(i).getNickname(), list.get(i).getDynamic(),
                                        list.get(i).getDynamicPictureUrl(), time,
                                        list.get(i).getLikeCount(), list.get(i).getCommentCount(), list.get(i).getUserType());
                                circleList.add(circle);
                            }
                        }
                    }
                }
            });
            mPage++;
            mRecyclerViewFinal.onLoadMoreComplete();//完成加载更多
        }
        mCircleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
