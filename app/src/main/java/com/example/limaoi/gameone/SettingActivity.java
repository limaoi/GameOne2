package com.example.limaoi.gameone;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;

/**
 * Created by limaoi on 2017/7/13.
 * E-mail：autismlm20@vip.qq.com
 */

public class SettingActivity extends BaseActivity implements OnClickListener {

    private Button mLogout;
    private Button mClearCache;
    private ImageView iv_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //初始化Bmob
        Bmob.initialize(this, "a21a5524eff971e709218fdd5420bec2");

        initViews();

        initEvents();
    }


    private void initViews() {
        mLogout = (Button) findViewById(R.id.bt_logout);
        mClearCache = (Button) findViewById(R.id.bt_clear_cache);
        iv_back = (ImageView) findViewById(R.id.iv_back);
    }

    private void initEvents() {
        mLogout.setOnClickListener(this);
        mClearCache.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_logout:
                BmobUser.logOut();   //清除缓存用户对象
                BmobUser currentUser = BmobUser.getCurrentUser(); // 现在的currentUser是null了
                Intent intent2 = new Intent(this, LoginActivity.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.bt_clear_cache:
                BmobQuery.clearAllCachedResults();
                finish();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
