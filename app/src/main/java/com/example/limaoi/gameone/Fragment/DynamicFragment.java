package com.example.limaoi.gameone.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.limaoi.gameone.R;
import com.example.limaoi.gameone.adapter.InfoAdapter;
import com.example.limaoi.gameone.bean.Info;
import com.example.limaoi.gameone.widget.MyDividerItemDecoration;

import java.util.ArrayList;

/**
 * Created by limaoi on 2017/7/5.
 * E-mail：autismlm20@vip.qq.com
 */

public class DynamicFragment extends Fragment {


    public static String TABLAYOUT_FRAGMENT = "tab_dy_fragment";
    private int type;
    private RecyclerView rv_recyclerView;
    private ArrayList<Info> infoList = new ArrayList<>();

    public static DynamicFragment newInstance(int type) {
        DynamicFragment fragment = new DynamicFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TABLAYOUT_FRAGMENT, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = (int) getArguments().getSerializable(TABLAYOUT_FRAGMENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dynamic, container, false);


        initData();

        initViews(view);

        initEvents();

        return view;
    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            Info info = new Info("姓名", "Autsim");
            infoList.add(info);
            Info info2 = new Info("姓名", "Autsim");
            infoList.add(info2);
            Info info3 = new Info("姓名", "Autsim");
            infoList.add(info3);
            Info info4 = new Info("姓名", "Autsim");
            infoList.add(info4);
        }
    }

    private void initViews(View view) {
        rv_recyclerView = (RecyclerView) view.findViewById(R.id.rv_recyclerView);
    }

    private void initEvents() {
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(getContext());
        rv_recyclerView.setLayoutManager(LinearLayoutManager);
        InfoAdapter InfoAdapter = new InfoAdapter(infoList);
        rv_recyclerView.setAdapter(InfoAdapter);
        rv_recyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    }
}
