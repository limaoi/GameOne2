package com.example.limaoi.gameone;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.limaoi.gameone.Fragment.MailRegisterFragment;
import com.example.limaoi.gameone.Fragment.PhoneRegisterFragment;



/**
 * Created by limaoi on 2017/6/23.
 * E-mail：autismlm20@vip.qq.com
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {


    private TextView tv_back;
    private TextView tv_login;
    private Button bt_phoneRegister;
    private Button bt_mailRegisterr;
    private PhoneRegisterFragment mPhoneRegisterFragment;
    private MailRegisterFragment mMailRegisterFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        MIUISetStatusBarLightMode(this.getWindow(), true);

        initViews();

        initEvents();
    }

    private void initViews() {
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_login = (TextView) findViewById(R.id.tv_login);
        bt_phoneRegister = (Button) findViewById(R.id.bt_phoneRegister);
        bt_mailRegisterr = (Button) findViewById(R.id.bt_mailRegisterr);
    }

    private void initEvents() {
        setDefaultFragment();
        tv_back.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        bt_phoneRegister.setOnClickListener(this);
        bt_mailRegisterr.setOnClickListener(this);
    }

    private void setDefaultFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (mMailRegisterFragment == null) {
            mMailRegisterFragment = new MailRegisterFragment();
        }
        transaction.replace(R.id.ly_rg_content, mMailRegisterFragment);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.bt_phoneRegister:
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                if (mPhoneRegisterFragment == null) {
                    mPhoneRegisterFragment = new PhoneRegisterFragment();
                }
                transaction.replace(R.id.ly_rg_content,
                        mPhoneRegisterFragment);
                transaction.commit();
                bt_phoneRegister.setVisibility(View.GONE);
                bt_mailRegisterr.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_mailRegisterr:
                FragmentTransaction transaction2 = getFragmentManager().beginTransaction();
                if (mMailRegisterFragment == null) {
                    mMailRegisterFragment = new MailRegisterFragment();
                }
                transaction2.replace(R.id.ly_rg_content,
                        mMailRegisterFragment);
                transaction2.commit();
                bt_phoneRegister.setVisibility(View.VISIBLE);
                bt_mailRegisterr.setVisibility(View.GONE);
                break;
            case R.id.tv_login:
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}
