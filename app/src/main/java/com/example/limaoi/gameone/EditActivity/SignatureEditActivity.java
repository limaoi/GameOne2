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

public class SignatureEditActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_back;
    private EditText et_edit_signature;
    private String signature;
    private TextView tv_submit;
    private ImageView iv_clean_signature;

    @Override
    protected void onStart() {
        BmobUser bmobUser = BmobUser.getCurrentUser();
        if (bmobUser != null) {
            signature = (String) BmobUser.getObjectByKey("signature");
            et_edit_signature.setText(signature);
            et_edit_signature.setSelection(signature.length());
        } else {

        }
        super.onStart();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_edit);

        MIUISetStatusBarLightMode(this.getWindow(), true);

        initViews();

        initEvents();
    }

    private void initViews() {
        et_edit_signature = (EditText) findViewById(R.id.et_edit_signature);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_clean_signature = (ImageView) findViewById(R.id.iv_clean_signature);
    }

    private void initEvents() {
        iv_back.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
        iv_clean_signature.setOnClickListener(this);

        et_edit_signature.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && iv_clean_signature.getVisibility() == View.GONE) {
                    iv_clean_signature.setVisibility(View.VISIBLE);
                }
                if (s.toString().matches(signature)) {
                    tv_submit.setVisibility(View.GONE);
                } else {
                    tv_submit.setVisibility(View.VISIBLE);
                }
                if (TextUtils.isEmpty(s)) {
                    tv_submit.setVisibility(View.GONE);
                    iv_clean_signature.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                final String content = et_edit_signature.getText().toString();
                Person newUser = new Person();
                newUser.setSignature(content);
                BmobUser bmobUser = BmobUser.getCurrentUser();
                newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toasty.success(SignatureEditActivity.this, "更新信息成功", Toast.LENGTH_SHORT, true).show();
                            Intent intent = new Intent();
                            intent.putExtra("signature_return", content);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            Toasty.error(SignatureEditActivity.this, "更新信息失败", Toast.LENGTH_SHORT, true).show();
                        }

                    }
                });
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_clean_signature:
                et_edit_signature.setText("");
                break;
        }
    }
}
