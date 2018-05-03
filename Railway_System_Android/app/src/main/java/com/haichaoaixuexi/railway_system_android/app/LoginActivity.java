package com.haichaoaixuexi.railway_system_android.app;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.haichaoaixuexi.railway_system_android.R;
import com.haichaoaixuexi.railway_system_android.data.Const;
import com.haichaoaixuexi.railway_system_android.entity.Users;
import com.haichaoaixuexi.railway_system_android.utils.GeneralUtil;
import com.haichaoaixuexi.railway_system_android.view.CustomVideoView;
import com.haichaoaixuexi.railway_system_android.view.JellyInterpolator;
import com.mob.MobSDK;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.haichaoaixuexi.railway_system_android.data.Const.REQUEST_CODE_SCAN;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.edt_uname)
    EditText edtUname;
    @BindView(R.id.edt_upwd)
    EditText edtUpwd;
    @BindView(R.id.main_btn_cancel)
    TextView mainBtnCancel;
    @BindView(R.id.main_btn_login)
    TextView mainBtnLogin;
    private CustomVideoView videoview;
    private View progress;
    private View mInputLayout;
    private float mWidth, mHeight;
    private LinearLayout mName, mPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        ButterKnife.bind(this);
        MobSDK.init(this);
    }

    /**
     * 初始化
     */
    private void initView() {
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        mName = (LinearLayout) findViewById(R.id.input_layout_name);
        mPsw = (LinearLayout) findViewById(R.id.input_layout_psw);

        videoview = (CustomVideoView) findViewById(R.id.videoview);
        videoview.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bgvideo1));
        //播放
        videoview.start();
        //循环播放
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoview.start();
            }
        });

    }
    public void animatorAchieve() {
        mWidth = mainBtnLogin.getMeasuredWidth();
        mHeight = mainBtnLogin.getMeasuredHeight();
        mName.setVisibility(View.INVISIBLE);
        mPsw.setVisibility(View.INVISIBLE);
        inputAnimator(mInputLayout, mWidth, mHeight);
    }
    @OnClick(R.id.main_btn_login)
    public void login() {
        final String uname = edtUname.getText().toString().trim();
        final String upwd = edtUpwd.getText().toString().trim();
        //输入验证
        if (checkInput(uname, upwd)) {
            //登陆动画效果
            animatorAchieve();
            //检查登陆结果
            checkLogin(uname, upwd);
            mainBtnLogin.setVisibility(View.INVISIBLE);
            mainBtnCancel.setVisibility(View.VISIBLE);
        }
    }
    private void checkLogin(final String uname, final String upwd) {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    OkHttpUtils.get().url(Const.URL_LOGIN)
                            .addParams("uname",uname)
                            .addParams("upwd",upwd)
                            .build().execute(new Callback() {
                        @Override
                        public Object parseNetworkResponse(Response response) throws IOException {
                            final String s = response.body().string();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (s.equals("failed")) {
                                        GeneralUtil.toast(LoginActivity.this, Const.MESSAGE_LOGHIN_FAILED);
                                    } else {
                                        GeneralUtil.startActivity(LoginActivity.this, StartActivity.class);
                                        GeneralUtil.toast(LoginActivity.this, Const.MESSAGE_LOGHIN_SUCCESS);
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
                                    GeneralUtil.toast(LoginActivity.this, Const.MESSAGE_LOGHIN_ERROR);
                                }
                            });
                        }
                        @Override
                        public void onResponse(Object response) {}
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GeneralUtil.toast(LoginActivity.this, Const.MESSAGE_LOGHIN_ERROR);

                        }
                    });
                }
            }
        });
        thread.start();
    }
    private boolean checkInput(String uname, String upwd) {
        if (uname == null || upwd == null || uname.equals("") || upwd.equals("")
                || uname == "" || upwd == "") {
            GeneralUtil.toast(this, "输入不能为空");
            return false;
        } else if (uname.length() > 10 || uname.length() < 3) {
            GeneralUtil.toast(this, "用户名格式错误");
            return false;
        }
        return true;
    }
    //
    //    //返回重启加载
    @Override
    protected void onRestart() {
        super.onRestart();
        initView();
    }
    //防止锁屏或者切出的时候，音乐在播放
    @Override
    protected void onStop() {
        super.onStop();
        videoview.stopPlayback();
    }
    private void inputAnimator(final View view, float w, float h) {
        AnimatorSet set = new AnimatorSet();
        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onAnimationCancel(Animator animator) { }
            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
    }
    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();
    }

    @OnClick(R.id.btn_login_back)
    public void goToStartActivity() {
        GeneralUtil.startActivity(LoginActivity.this,StartActivity.class);
    }
    @OnClick(R.id.main_btn_cancel)
    public void cancel() {
        this.recreate();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            GeneralUtil.startActivity(LoginActivity.this,StartActivity.class);
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.btn_scan_login)
    public void scanForLogin(){
        Intent intent = new Intent(LoginActivity.this, CaptureActivity.class);
         /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
         * 也可以不传这个参数
         * 不传的话  默认都为默认不震动  其他都为true
         * */

        ZxingConfig config = new ZxingConfig();
        //config.setShowbottomLayout(true);//底部布局（包括闪光灯和相册）
        config.setPlayBeep(true);//是否播放提示音
        config.setShake(true);//是否震动
        config.setShowAlbum(true);//是否显示相册
        config.setShowFlashLight(true);//是否显示闪光灯
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(intent, REQUEST_CODE_SCAN);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    1);}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }
    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
            } else {
                // Permission Denied
                //  displayFrameworkBugMessageAndExit();
                Toast.makeText(this, "请在应用管理中打开“相机”访问权限！", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                //二维码内容
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                char type = content.charAt(0);
                content = content.substring(1);
                if (type==Const.TYPE_USER&&GeneralUtil.isMobiPhoneNum(content)){
                    //界面跳转
                    Intent intent = new Intent(LoginActivity.this,VerificationCodeActivity.class);
                    intent.putExtra("country","86");
                    intent.putExtra("phone",content);
                    startActivity(intent);
                }else {
                    GeneralUtil.toast(LoginActivity.this,"二维码类别不正确");
                }
            }
        }
    }
}
