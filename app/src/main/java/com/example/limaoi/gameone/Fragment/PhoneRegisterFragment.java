package com.example.limaoi.gameone.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import es.dmoral.toasty.Toasty;

/**
 * Created by limaoi on 2017/6/23.
 * E-mail：autismlm20@vip.qq.com
 */

public class PhoneRegisterFragment extends Fragment implements View.OnClickListener {

    private EditText et_phone;
    private EditText et_verification;
    private EditText et_password;

    private Button bt_getVerification;
    private Button bt_submit;

    private ImageView iv_clean_phone;
    private ImageView iv_clean_verification;
    private ImageView iv_clean_password;
    private ImageView iv_show_pwd;

    private TextView tv_hintPwd;
    private TimeCount time;

    private static final String headPictureUrl = "http://bmob-cdn-13327.b0.upaiyun.com/2018/01/08/f8f99045cc8c42a8aff2a60f99c6b30f.png";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_register, container, false);

        //初始化控件
        initViews(view);

        //初始化事件
        initEvents();

        Bmob.initialize(getActivity(), "a21a5524eff971e709218fdd5420bec2");

        time = new TimeCount(60000, 1000); //实例化对象

        return view;
    }

    private void initViews(View view) {
        et_phone = (EditText) view.findViewById(R.id.et_phone);
        et_verification = (EditText) view.findViewById(R.id.et_verification);
        et_password = (EditText) view.findViewById(R.id.et_password);
        bt_getVerification = (Button) view.findViewById(R.id.bt_getVerification);
        bt_submit = (Button) view.findViewById(R.id.bt_submit);
        iv_clean_phone = (ImageView) view.findViewById(R.id.iv_clean_phone);
        iv_clean_verification = (ImageView) view.findViewById(R.id.iv_clean_verification);
        iv_clean_password = (ImageView) view.findViewById(R.id.iv_clean_password);
        iv_show_pwd = (ImageView) view.findViewById(R.id.iv_show_pwd);
        tv_hintPwd = (TextView) view.findViewById(R.id.tv_hintPwd);
    }

    private void initEvents() {
        et_phone.setOnClickListener(this);
        et_verification.setOnClickListener(this);
        et_password.setOnClickListener(this);
        bt_getVerification.setOnClickListener(this);
        bt_submit.setOnClickListener(this);
        iv_clean_phone.setOnClickListener(this);
        iv_clean_verification.setOnClickListener(this);
        iv_clean_password.setOnClickListener(this);
        iv_show_pwd.setOnClickListener(this);
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && iv_clean_phone.getVisibility() == View.GONE) {
                    iv_clean_phone.setVisibility(View.VISIBLE);
                }
                if (TextUtils.isEmpty(s)) {
                    iv_clean_phone.setVisibility(View.GONE);
                    bt_submit.setEnabled(false);
                }
                if (et_phone.getText().toString().length() == 11 && et_password.getText().toString().length() != 0 && et_password.getText().toString().matches("[A-Za-z0-9]+") && et_verification.getText().toString().length() == 6) {
                    bt_submit.setEnabled(true);
                }
                if (et_phone.getText().toString().length() != 11) {
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
                if (!TextUtils.isEmpty(s) && iv_clean_password.getVisibility() == View.GONE) {
                    iv_clean_password.setVisibility(View.VISIBLE);
                    iv_show_pwd.setVisibility(View.VISIBLE);
                }
                if (TextUtils.isEmpty(s)) {
                    iv_clean_password.setVisibility(View.GONE);
                    iv_show_pwd.setVisibility(View.GONE);
                    tv_hintPwd.setText("");
                    tv_hintPwd.setVisibility(View.GONE);
                    bt_submit.setEnabled(false);
                }
                if (s.toString().matches("[A-Za-z0-9]+")) {
                    tv_hintPwd.setText("");
                    tv_hintPwd.setVisibility(View.GONE);
                    if (et_phone.getText().toString().length() == 11 && et_password.getText().toString().length() != 0 && et_password.getText().toString().matches("[A-Za-z0-9]+") && et_verification.getText().toString().length() == 6) {
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

        et_verification.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && iv_clean_verification.getVisibility() == View.INVISIBLE) {
                    iv_clean_verification.setVisibility(View.VISIBLE);
                }
                if (TextUtils.isEmpty(s)) {
                    iv_clean_verification.setVisibility(View.INVISIBLE);
                    bt_submit.setEnabled(false);
                }
                if (et_phone.getText().toString().length() == 11 && et_password.getText().toString().length() != 0 && et_password.getText().toString().matches("[A-Za-z0-9]+") && et_verification.getText().toString().length() == 6) {
                    bt_submit.setEnabled(true);
                }
                if (et_verification.getText().toString().length() != 6) {
                    bt_submit.setEnabled(false);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clean_phone:
                et_phone.setText("");
                break;
            case R.id.iv_clean_password:
                et_password.setText("");
                tv_hintPwd.setText("");
                tv_hintPwd.setVisibility(View.GONE);
                break;
            case R.id.iv_clean_verification:
                et_verification.setText("");
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
            case R.id.bt_getVerification:
                String phoneNum = et_phone.getText().toString();
                if (phoneNum.length() != 11) {
                    Toasty.error(getActivity(), "请输入11位手机号码", Toast.LENGTH_SHORT, true).show();
                } else {

                    BmobSMS.requestSMSCode(phoneNum, "注册验证码", new QueryListener<Integer>() {
                        @Override
                        public void done(Integer integer, BmobException e) {
                            if (e == null) {//验证码发送成功
                                Log.i("bmob", "短信id：" + integer);//用于查询本次短信发送详情
                                time.start();
                            } else {
                                if (isNetworkConnected(getActivity())) {
                                    Toasty.error(getActivity(), "获取验证码失败", Toast.LENGTH_SHORT, true).show();
                                } else {
                                    Toasty.error(getActivity(), "网络不可用", Toast.LENGTH_SHORT, true).show();
                                }
                            }
                        }
                    });
                }
                break;
            case R.id.bt_submit:
                BmobSMS.verifySmsCode(et_phone.getText().toString(), et_verification.getText().toString(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.i("bmob", "验证通过");
                            String number = getStringRandom(8);
                            Person person = new Person();
                            person.setUsername(et_phone.getText().toString());
                            person.setMobilePhoneNumber(et_phone.getText().toString());
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
                                            Toasty.error(getActivity(), "手机号已被注册", Toast.LENGTH_SHORT, true).show();
                                        } else {
                                            Toasty.error(getActivity(), "网络不可用", Toast.LENGTH_SHORT, true).show();
                                        }
                                    }
                                }
                            });

                        } else {
                            if (isNetworkConnected(getActivity())) {
                                Toasty.error(getActivity(), "验证码错误", Toast.LENGTH_SHORT, true).show();
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

    class TimeCount extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            bt_getVerification.setEnabled(false);
            bt_getVerification.setTextColor(Color.parseColor("#999999"));
            bt_getVerification.setText(millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            bt_getVerification.setText("获取验证码");
            bt_getVerification.setEnabled(true);
            bt_getVerification.setTextColor(Color.parseColor("#d81e06"));
        }
    }

}

