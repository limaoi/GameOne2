package com.example.limaoi.gameone.Fragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.limaoi.gameone.R;
import com.example.limaoi.gameone.adapter.TabAdapter;
import com.example.limaoi.gameone.adapter.TabVideoAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by limaoi on 2017/6/18.
 * E-mail：autismlm20@vip.qq.com
 */

public class VideoFragment extends BaseFragment {


    private TabLayout mTableLayout;
    private ViewPager mViewPager;
    private TabVideoAdapter adapter;
    private List<String> mTitles;
    public static final String[] tabTitle = new String[]{"英雄联盟", "王者荣耀", "绝地求生", "职业联赛", "主播糗事", "其他"};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MIUISetStatusBarLightMode(getActivity().getWindow(), true);

        initViews(view);

        initEvents();

        return view;
    }

    private void initViews(View view) {
        mTableLayout = (TabLayout) view.findViewById(R.id.ty_tab);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_viewpager);
    }

    private void initEvents() {
        mTitles = new ArrayList<>();
        for (int i = 0; i < tabTitle.length; i++) {
            mTitles.add(tabTitle[i]);
        }

        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < tabTitle.length / 2; i++) {
            fragments.add(LOLVideoFragment.newInstance(i + 1));
            fragments.add(KingGloryVideoFragment.newInstance(i + 1));
        }
        adapter = new TabVideoAdapter(this.getChildFragmentManager(), fragments, mTitles);
        //给ViewPager设置适配器
        mViewPager.setAdapter(adapter);
        //将TabLayout和ViewPager关联
        mTableLayout.setupWithViewPager(mViewPager);
        //设置可以滑动
        mTableLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }
}
