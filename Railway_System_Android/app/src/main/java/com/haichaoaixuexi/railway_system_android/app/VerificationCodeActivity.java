package com.haichaoaixuexi.railway_system_android.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haichaoaixuexi.railway_system_android.R;
import com.haichaoaixuexi.railway_system_android.data.Const;
import com.haichaoaixuexi.railway_system_android.entity.Users;
import com.haichaoaixuexi.railway_system_android.utils.GeneralUtil;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tuo.customview.VerificationCodeView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
/**
 * Created by haichao on 2018/2/22.
 * 至尊宝：长夜漫漫无心睡眠，我以为只有我睡不着，原来晶晶姑娘你也睡不着 ！
 * describe:手机验证码界面
 */
public class VerificationCodeActivity extends BaseActivity {

    @BindView(R.id.icv)
    VerificationCodeView icv;
    @BindView(R.id.btn_VerificationCode)
    Button btnVerificationCode;
    @BindView(R.id.txt_reGetCode)
    TextView txtReGetCode;
    @BindView(R.id.btn_reGetCode)
    Button btnReGetCode;
    @BindView(R.id.txt_connPhone)
    TextView txtConnPhone;
    String country = "";
    String phone = "";
    //倒计时（秒）
    final static int COUNT_TIME = 30;
    //验证码长度
    final static int COUNT_CODE = 6;
    @BindView(R.id.btn_code_back)
    ImageView btnCodeBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smscode);
        ButterKnife.bind(this);
        //设置验证码数量
        icv.setEtNumber(COUNT_CODE);
        //设置倒计时
        countDownTimer(COUNT_TIME);
        //显示发送验证码的手机号
        final Intent intent = getIntent();
        country = intent.getStringExtra("country");
        phone = intent.getStringExtra("phone");
        sendCode(country, phone);
        txtConnPhone.setText("验证码已发送至关联手机：+" + country + " " + phone + "");

    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }


    public void countDownTimer(int time) {
        CountDownTimer timer = new CountDownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long l) {
                btnReGetCode.setVisibility(View.GONE);
                txtReGetCode.setVisibility(View.VISIBLE);
                txtReGetCode.setText("重新获取验证码 (" + l / 1000 + "秒)");
            }

            @Override
            public void onFinish() {
                btnReGetCode.setVisibility(View.VISIBLE);
                txtReGetCode.setVisibility(View.GONE);
            }
        }.start();
    }

    @OnClick(R.id.btn_VerificationCode)
    public void VerificationCode() {
        if (icv.getInputContent().length() == COUNT_CODE)
            submitCode(country, phone, icv.getInputContent());
        else
            GeneralUtil.toast(VerificationCodeActivity.this, "验证码错误");

    }

    @OnClick(R.id.btn_reGetCode)
    public void reGetCode() {
        countDownTimer(COUNT_TIME);
        sendCode(country, phone);
    }

    // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
    public void sendCode(String country, String phone) {
        // 注册一个事件回调，用于处理发送验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理成功得到验证码的结果
                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                } else {
                    // TODO 处理错误的结果
                }

            }
        });
        // 触发操作
        SMSSDK.getVerificationCode(country, phone);
    }

    // 提交验证码，其中的code表示验证码，如“1357”
    public void submitCode(String country, final String phone, String code) {
        // 注册一个事件回调，用于处理提交验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理验证成功的结果
                    //Log.e("tag","success");
                    /*runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GeneralUtil.toast(VerificationCodeActivity.this,"登陆成功");

                        }
                    });*/
                    getUserInfo(phone);
                } else {
                    // TODO 处理错误的结果
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GeneralUtil.toast(VerificationCodeActivity.this, "验证码错误");
                        }
                    });
                }
            }
        });
        // 触发操作
        SMSSDK.submitVerificationCode(country, phone, code);
    }

    protected void onDestroy() {
        super.onDestroy();
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler();
    }

    private void getUserInfo(final String phone) {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpUtils.get().url(Const.URL_GET_USER_BY_PHONE)
                            .addParams("uphone", phone)
                            .build().execute(new Callback() {
                        @Override
                        public Object parseNetworkResponse(Response response) throws IOException {
                            final String s = response.body().string();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (s.equals("failed")) {
                                        GeneralUtil.toast(VerificationCodeActivity.this, Const.MESSAGE_USER_NO_EXIST);
                                    } else {
                                        GeneralUtil.startActivity(VerificationCodeActivity.this, StartActivity.class);
                                        GeneralUtil.toast(VerificationCodeActivity.this, Const.MESSAGE_LOGHIN_SUCCESS);
                                        Gson gson = new Gson();
                                        Const.currentuser = gson.fromJson(s, Users.class);
                                    }
                                }
                            });
                            return null;
                        }

                        @Override
                        public void onError(Request request, Exception e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    GeneralUtil.toast(VerificationCodeActivity.this, Const.MESSAGE_LOGHIN_ERROR);
                                }
                            });
                        }

                        @Override
                        public void onResponse(Object response) {
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GeneralUtil.toast(VerificationCodeActivity.this, Const.MESSAGE_LOGHIN_ERROR);
                        }
                    });
                }
            }
        });
        thread.start();
    }
    @OnClick(R.id.btn_code_back)
    public void backToLogin(){
        GeneralUtil.startActivity(VerificationCodeActivity.this,LoginActivity.class);
    }
}
