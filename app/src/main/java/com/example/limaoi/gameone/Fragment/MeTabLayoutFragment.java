package com.example.limaoi.gameone.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.limaoi.gameone.EditInfoActivity;
import com.example.limaoi.gameone.LoginActivity;
import com.example.limaoi.gameone.R;
import com.example.limaoi.gameone.adapter.InfoAdapter;
import com.example.limaoi.gameone.bean.Info;
import com.example.limaoi.gameone.widget.MyDividerItemDecoration;

import java.util.ArrayList;

import cn.bmob.v3.BmobUser;

/**
 * Created by limaoi on 2017/7/4.
 * E-mail：autismlm20@vip.qq.com
 */

public class MeTabLayoutFragment extends Fragment implements View.OnClickListener {

    public static String TABLAYOUT_FRAGMENT = "tab_me_fragment";
    private int type;

    private RelativeLayout ry_compile;
    private LinearLayout ly_content_blank;
    private Button bt_login;
    private RecyclerView rv_recyclerView;
    private ArrayList<Info> infoList = new ArrayList<>();


    @Override
    public void onStart() {
        BmobUser bmobUser = BmobUser.getCurrentUser();
        if (bmobUser != null) {
            infoList.clear();
            rv_recyclerView.setVisibility(View.VISIBLE);
            ly_content_blank.setVisibility(View.GONE);
            ry_compile.setVisibility(View.VISIBLE);
            String nickname = (String) BmobUser.getObjectByKey("nickname");
            String signature = (String) BmobUser.getObjectByKey("signature");
            String sex = (String) BmobUser.getObjectByKey("sex");
            String address = (String) BmobUser.getObjectByKey("address");
            String email = (String) BmobUser.getObjectByKey("email");
            String mobilePhoneNumber = (String) BmobUser.getObjectByKey("mobilePhoneNumber");
            Info info = new Info("昵称", nickname);
            infoList.add(info);
            Info info2 = new Info("个人签名", signature);
            infoList.add(info2);
            Info info3 = new Info("性别", sex);
            infoList.add(info3);
            Info info4 = new Info("所在地", address);
            infoList.add(info4);
            if (email == null) {
                Info info5 = new Info("邮箱", "未填写");
                infoList.add(info5);
            } else {
                Info info5 = new Info("邮箱", email);
                infoList.add(info5);
            }
            if (mobilePhoneNumber == null) {
                Info info6 = new Info("手机", "未填写");
                infoList.add(info6);
            } else {
                Info info6 = new Info("手机", mobilePhoneNumber);
                infoList.add(info6);
            }
        } else {
            rv_recyclerView.setVisibility(View.GONE);
            ly_content_blank.setVisibility(View.VISIBLE);
            ry_compile.setVisibility(View.GONE);
        }
        super.onStart();
    }

    public static MeTabLayoutFragment newInstance(int type) {
        MeTabLayoutFragment fragment = new MeTabLayoutFragment();
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_me_tablayout, container, false);

        initViews(view);

        initEvents();
        return view;
    }


    private void initViews(View view) {
        ry_compile = (RelativeLayout) view.findViewById(R.id.ry_compile);
        ly_content_blank = (LinearLayout) view.findViewById(R.id.ly_content_blank);
        rv_recyclerView = (RecyclerView) view.findViewById(R.id.rv_recyclerView);
        bt_login = (Button) view.findViewById(R.id.bt_login);
    }


    private void initEvents() {
        bt_login.setOnClickListener(this);
        ry_compile.setOnClickListener(this);


        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(getContext());
        rv_recyclerView.setLayoutManager(LinearLayoutManager);
        InfoAdapter InfoAdapter = new InfoAdapter(infoList);
        rv_recyclerView.setAdapter(InfoAdapter);
        rv_recyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.ry_compile:
                Intent intent1 = new Intent(getActivity(), EditInfoActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
