package com.example.limaoi.gameone.EditActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.limaoi.gameone.BaseActivity;
import com.example.limaoi.gameone.R;
import com.example.limaoi.gameone.bean.Person;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import es.dmoral.toasty.Toasty;

/**
 * Created by limaoi on 2017/7/22.
 * E-mail：autismlm20@vip.qq.com
 */

public class PhoneEditActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_back;
    private EditText et_edit_phone;
    private TextView tv_submit;
    private ImageView iv_clean_phone;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_edit);

        MIUISetStatusBarLightMode(this.getWindow(), true);

        initViews();

        initEvents();

    }

    private void initViews() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        et_edit_phone = (EditText) findViewById(R.id.et_edit_phone);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        iv_clean_phone = (ImageView) findViewById(R.id.iv_clean_phone);
    }

    private void initEvents() {
        iv_back.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
        iv_clean_phone.setOnClickListener(this);
        et_edit_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() != 0) {
                    iv_clean_phone.setVisibility(View.VISIBLE);
                } else {
                    iv_clean_phone.setVisibility(View.GONE);
                }
                if (s.toString().length() == 11) {
                    tv_submit.setVisibility(View.VISIBLE);
                } else {
                    tv_submit.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clean_phone:
                et_edit_phone.setText("");
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_submit:
                if (isRightPhone()) {
                    final String content = et_edit_phone.getText().toString();
                    Person newUser = new Person();
                    newUser.setMobilePhoneNumber(content);
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toasty.success(PhoneEditActivity.this, "更新信息成功", Toast.LENGTH_SHORT, true).show();
                                Intent intent = new Intent();
                                intent.putExtra("phone_return", content);
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                Toasty.error(PhoneEditActivity.this, "更新信息失败", Toast.LENGTH_SHORT, true).show();
                            }

                        }
                    });
                } else {
                    Toasty.error(PhoneEditActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT, true).show();
                }
                break;
        }
    }

    public boolean isRightPhone() {
        boolean isOfRight;
        String phone = et_edit_phone.getText().toString();
        if (phone != null & phone.length() == 11) {
            isOfRight = true;
        } else {
            isOfRight = false;
        }
        return isOfRight;
    }
}
