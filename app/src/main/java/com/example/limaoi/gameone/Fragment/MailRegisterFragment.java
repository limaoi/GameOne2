package com.example.limaoi.gameone.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.limaoi.gameone.R;
import com.example.limaoi.gameone.bean.Person;

import java.util.Random;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import es.dmoral.toasty.Toasty;

/**
 * Created by limaoi on 2017/6/23.
 * E-mail：autismlm20@vip.qq.com
 */

public class MailRegisterFragment extends Fragment implements View.OnClickListener {

    private EditText et_mail;
    private EditText et_password;
    private Button bt_submit;
    private ImageView iv_clean_mail;
    private ImageView iv_show_pwd;
    private ImageView clean_password;
    private TextView tv_hintMail;
    private TextView tv_hintPwd;

    private static final String headPictureUrl = "http://bmob-cdn-13327.b0.upaiyun.com/2018/01/08/f8f99045cc8c42a8aff2a60f99c6b30f.png";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mail_register, container, false);

        //初始化控件
        initViews(view);

        //初始化事件
        initEvents();

        //初始化Bmob
        Bmob.initialize(getActivity(), "a21a5524eff971e709218fdd5420bec2");
        return view;
    }


    private void initViews(View view) {
        et_mail = (EditText) view.findViewById(R.id.et_mail);
        et_password = (EditText) view.findViewById(R.id.et_password);
        bt_submit = (Button) view.findViewById(R.id.bt_submit);
        iv_clean_mail = (ImageView) view.findViewById(R.id.iv_clean_mail);
        iv_show_pwd = (ImageView) view.findViewById(R.id.iv_show_pwd);
        clean_password = (ImageView) view.findViewById(R.id.clean_password);
        tv_hintMail = (TextView) view.findViewById(R.id.tv_hintMail);
        tv_hintPwd = (TextView) view.findViewById(R.id.tv_hintPwd);
    }

    private void initEvents() {
        et_mail.setOnClickListener(this);
        et_password.setOnClickListener(this);
        bt_submit.setOnClickListener(this);
        iv_clean_mail.setOnClickListener(this);
        iv_show_pwd.setOnClickListener(this);
        clean_password.setOnClickListener(this);
        bt_submit.setOnClickListener(this);
        et_mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && iv_clean_mail.getVisibility() == View.GONE) {
                    iv_clean_mail.setVisibility(View.VISIBLE);
                }
                if (TextUtils.isEmpty(s)) {
                    iv_clean_mail.setVisibility(View.GONE);
                    tv_hintMail.setText("");
                    tv_hintMail.setVisibility(View.GONE);
                    bt_submit.setEnabled(false);
                }
                if (!TextUtils.isEmpty(s) && s.toString().matches("[a-zA-Z0-9._-]+@[a-z0-9].+[a-z]+")) {
                    tv_hintMail.setText("");
                    tv_hintMail.setVisibility(View.GONE);
                    if (et_mail.getText().toString().length() != 0 && et_password.getText().toString().length() != 0 && et_password.getText().toString().matches("[A-Za-z0-9]+")) {
                        bt_submit.setEnabled(true);
                    }
                }
                if (!TextUtils.isEmpty(s) && !s.toString().matches("[a-zA-Z0-9._-]+@[a-z0-9].+[a-z]+")) {
                    tv_hintMail.setText("请输入正确的邮箱");
                    tv_hintMail.setVisibility(View.VISIBLE);
                    bt_submit.setEnabled(false);
                }
            }
        });
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && clean_password.getVisibility() == View.GONE) {
                    clean_password.setVisibility(View.VISIBLE);
                    iv_show_pwd.setVisibility(View.VISIBLE);
                }
                if (TextUtils.isEmpty(s)) {
                    clean_password.setVisibility(View.GONE);
                    iv_show_pwd.setVisibility(View.GONE);
                    tv_hintPwd.setText("");
                    tv_hintPwd.setVisibility(View.GONE);
                    bt_submit.setEnabled(false);
                }
                if (!TextUtils.isEmpty(s) && s.toString().matches("[A-Za-z0-9]+")) {
                    tv_hintPwd.setText("");
                    tv_hintPwd.setVisibility(View.GONE);
                    if (et_mail.getText().toString().length() != 0 && et_password.getText().toString().length() != 0 && et_mail.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z0-9].+[a-z]+")) {
                        bt_submit.setEnabled(true);
                    }
                }
                if (!TextUtils.isEmpty(s) && !s.toString().matches("[A-Za-z0-9]+")) {
                    tv_hintPwd.setText("请输入数字或字母");
                    tv_hintPwd.setVisibility(View.VISIBLE);
                    bt_submit.setEnabled(false);
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clean_mail:
                et_mail.setText("");
                tv_hintMail.setText("");
                tv_hintMail.setVisibility(View.GONE);
                break;
            case R.id.clean_password:
                et_password.setText("");
                tv_hintPwd.setText("");
                tv_hintPwd.setVisibility(View.GONE);
                break;
            case R.id.iv_show_pwd:
                if (et_password.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_show_pwd.setImageResource(R.mipmap.pass_visuable);
                } else {
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_show_pwd.setImageResource(R.mipmap.pass_gone);
                }
                String pwd = et_password.getText().toString();
                if (!TextUtils.isEmpty(pwd))
                    et_password.setSelection(pwd.length());
                break;
            case R.id.bt_submit:
                String number = getStringRandom(8);
                Person person = new Person();
                person.setUsername(et_mail.getText().toString());
                person.setEmail(et_mail.getText().toString());
                person.setPassword(et_password.getText().toString());
                person.setUserType("1");
                person.setNickname("用户" + number);
                person.setSignature("这个人很懒什么都没有留下");
                person.setAddress("未填写");
                person.setSex("未填写");
                person.setPic(headPictureUrl);
                person.signUp(new SaveListener<Object>() {
                    @Override
                    public void done(Object o, BmobException e) {
                        if (e == null) {
                            Toasty.success(getActivity(), "注册成功", Toast.LENGTH_SHORT, true).show();
                            BmobUser.logOut();
                            getActivity().finish();
                        } else {
                            if (isNetworkConnected(getActivity())) {
                                Log.i("bmob", "error" + e);
                                Toasty.error(getActivity(), "邮箱已被注册", Toast.LENGTH_SHORT, true).show();
                            } else {
                                Toasty.error(getActivity(), "网络不可用", Toast.LENGTH_SHORT, true).show();
                            }
                        }
                    }
                });
                break;
        }
    }


    //生成随机数字和字母,
    public String getStringRandom(int length) {

        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }


    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
