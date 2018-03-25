package com.example.limaoi.gameone;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.limaoi.gameone.Fragment.CircleFragment;
import com.example.limaoi.gameone.Fragment.HomeFragment;
import com.example.limaoi.gameone.Fragment.MeFragment;
import com.example.limaoi.gameone.Fragment.VideoFragment;
import com.example.limaoi.gameone.adapter.TabPageAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import es.dmoral.toasty.Toasty;


/**
 * Created by limaoi on 2017/6/17.
 * E-mail：autismlm20@vip.qq.com
 */

public class MainActivity extends BaseCheckPermissionsActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private boolean isExit;
    private ViewPager mViewPager;
    private List<Fragment> mFragments = new ArrayList<>();
    private TabPageAdapter mTabPageAdapter;
    private RadioGroup mRadioGroup;
    private RadioButton homeRadioButton;
    private RadioButton videoRadioButton;
    private RadioButton circleRadioButton;
    private RadioButton meRadioButton;
    private FloatingActionButton fab_add;
    private Dialog dialog;

    @SuppressLint("HandlerLeak")
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragment();

        //初始化控件
        initView();

        //初始化事件
        initEvents();
    }

    private void initFragment() {
        HomeFragment homeFragment = new HomeFragment();
        VideoFragment videoFragment = new VideoFragment();
        CircleFragment circleFragment = new CircleFragment();
        MeFragment meFragment = new MeFragment();
        mFragments.add(homeFragment);
        mFragments.add(videoFragment);
        mFragments.add(circleFragment);
        mFragments.add(meFragment);
    }

    private void initView() {
        mTabPageAdapter = new TabPageAdapter(getSupportFragmentManager(), mFragments);
        mViewPager = (ViewPager) findViewById(R.id.vp_viewPager);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        homeRadioButton = (RadioButton) findViewById(R.id.rb_home);
        videoRadioButton = (RadioButton) findViewById(R.id.rb_video);
        circleRadioButton = (RadioButton) findViewById(R.id.rb_circle);
        meRadioButton = (RadioButton) findViewById(R.id.rb_me);
        fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
    }

    private void initEvents() {
        mViewPager.setAdapter(mTabPageAdapter);
        mViewPager.addOnPageChangeListener(this);
        mTabPageAdapter.updateData(mFragments);
        fab_add.setOnClickListener(this);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        selectPage(0);
                        break;
                    case R.id.rb_video:
                        selectPage(1);
                        break;
                    case R.id.rb_circle:
                        selectPage(2);
                        break;
                    case R.id.rb_me:
                        selectPage(3);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void selectPage(int position) {
        switch (position) {
            case 0:
                homeRadioButton.setChecked(true);
                break;
            case 1:
                videoRadioButton.setChecked(true);
                break;
            case 2:
                circleRadioButton.setChecked(true);
                break;
            case 3:
                meRadioButton.setChecked(true);
                break;
        }
        mViewPager.setCurrentItem(position, false);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isExit) {
                isExit = true;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mhandler.sendEmptyMessageDelayed(0, 2000);
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                System.exit(0);
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    //初始化Dialog
    private void showDialog() {
        dialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.add_dialog, null);
        dialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        bindDialogEvent(contentView);
    }

    private void bindDialogEvent(View contentView) {
        LinearLayout ll_wb = (LinearLayout) contentView.findViewById(R.id.ll_wb);
        LinearLayout ll_tp = (LinearLayout) contentView.findViewById(R.id.ll_tp);
        LinearLayout ll_sp = (LinearLayout) contentView.findViewById(R.id.ll_sp);

        ll_sp.setOnClickListener(this);
        ll_tp.setOnClickListener(this);
        ll_wb.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add:
                showDialog();
                break;
            case R.id.ll_wb:
                BmobUser bmobUser = BmobUser.getCurrentUser();
                if (bmobUser != null) {
                    String picurl = (String) BmobUser.getObjectByKey("pic");
                    if (picurl == null) {
                        Toasty.error(this, "请先上传头像", Toast.LENGTH_SHORT, true).show();
                    } else {
                        Intent intent = new Intent(this, EditMsgActivity.class);
                        startActivity(intent);
                        dialog.cancel();
                        break;
                    }
                    break;
                } else {
                    Toasty.error(this, "请先登录", Toast.LENGTH_SHORT, true).show();
                }
            case R.id.ll_tp:

                break;
            case R.id.ll_sp:
                BmobUser bmobUser2 = BmobUser.getCurrentUser();
                if (bmobUser2 != null) {
                    String userType = (String) BmobUser.getObjectByKey("userType");
                    if (userType.equals("1")) {
                        Toasty.error(this, "请申请作者或认证", Toast.LENGTH_SHORT, true).show();
                    } else {
                        Intent intent = new Intent(this, AddVideoActivity.class);
                        startActivity(intent);
                        dialog.cancel();
                    }
                } else {
                    Toasty.error(this, "请先登录", Toast.LENGTH_SHORT, true).show();
                }
                break;
        }
    }
}
