package com.example.limaoi.gameone.EditActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
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

public class EmailEditActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_back;
    private EditText et_edit_email;
    private TextView tv_submit;
    private String email;
    private ImageView iv_clean_email;


    @Override
    protected void onStart() {

        BmobUser bmobUser = BmobUser.getCurrentUser();
        if (bmobUser != null) {
            email = (String) BmobUser.getObjectByKey("email");
            if (email != null) {
                et_edit_email.setText(email);
                et_edit_email.setSelection(email.length());
            }
        } else {
            et_edit_email.setText(null);
        }


        super.onStart();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_edit);

        MIUISetStatusBarLightMode(this.getWindow(), true);

        initViews();

        initEvents();

    }

    private void initViews() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        et_edit_email = (EditText) findViewById(R.id.et_edit_email);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        iv_clean_email = (ImageView) findViewById(R.id.iv_clean_email);
    }

    private void initEvents() {
        iv_back.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
        iv_clean_email.setOnClickListener(this);
        et_edit_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    tv_submit.setVisibility(View.VISIBLE);
                    iv_clean_email.setVisibility(View.VISIBLE);
                } else {
                    tv_submit.setVisibility(View.GONE);
                    iv_clean_email.setVisibility(View.GONE);
                }
                if (email != null) {
                    if (s.toString().matches(email)) {
                        tv_submit.setVisibility(View.GONE);
                    } else {
                        tv_submit.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clean_email:
                et_edit_email.setText("");
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_submit:
                if (isRightEmail()) {
                    final String content = et_edit_email.getText().toString();
                    Person newUser = new Person();
                    newUser.setEmail(content);
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toasty.success(EmailEditActivity.this, "更新信息成功", Toast.LENGTH_SHORT, true).show();
                                Intent intent = new Intent();
                                intent.putExtra("eamil_return", content);
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                Toasty.error(EmailEditActivity.this, "请检查邮箱是否已被注册", Toast.LENGTH_SHORT, true).show();
                            }
                        }
                    });
                } else {
                    Toasty.error(EmailEditActivity.this, "请输入正确的邮箱", Toast.LENGTH_SHORT, true).show();
                }

                break;
        }
    }

    public boolean isRightEmail() {
        boolean isOfright;
        String email = et_edit_email.getText().toString();
        if (email != null && email.matches("[a-zA-Z0-9._-]+@[a-z0-9].+[a-z]+")) {
            isOfright = true;
        } else {
            isOfright = false;
        }
        return isOfright;
    }
}
