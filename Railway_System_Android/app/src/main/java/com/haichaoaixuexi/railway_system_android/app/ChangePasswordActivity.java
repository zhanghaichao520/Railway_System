package com.haichaoaixuexi.railway_system_android.app;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haichaoaixuexi.railway_system_android.R;
import com.haichaoaixuexi.railway_system_android.data.Const;
import com.haichaoaixuexi.railway_system_android.entity.Users;
import com.haichaoaixuexi.railway_system_android.utils.GeneralUtil;
import com.haichaoaixuexi.railway_system_android.utils.MD5;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zwy.kutils.utils.Log;
import com.zwy.kutils.widget.loadingdialog.DialogUIUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePasswordActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.edt_upwd)
    EditText edtUpwd;
    @BindView(R.id.edt_new_pwd)
    EditText edtNewPwd;
    @BindView(R.id.edt_pwd_confirm)
    EditText edtPwdConfirm;
    @BindView(R.id.btn_change_pwd)
    Button btnChangePwd;
    String upwd;
    String newPwd;
    String confirmPwd;
    MD5 md5 = new MD5();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_changepassword);
        ButterKnife.bind(this);
        titleTxt.setText(R.string.pwdHint);
    }

    @Override
    protected void initData() {}
    @OnClick(R.id.btn_back)
    public void backToMain() {
        readyGoThenKill(StartActivity.class);
    }
    @OnClick(R.id.btn_change_pwd)
    public void changePwd(){
        upwd = edtUpwd.getText().toString().trim();
        newPwd = edtNewPwd.getText().toString().trim();
        confirmPwd = edtPwdConfirm.getText().toString().trim();
        upwd = md5.getMD5ofStr(upwd);
        if (upwd.equals("")||newPwd.equals("")||confirmPwd.equals("")
                ||upwd==null||newPwd==null||confirmPwd==null){
            showToast("输入不可以为空");
            return;
        }
        if (!upwd.equals(Const.currentuser.getUSER_PWD())){
            showToast("原密码不正确");
            return;
        }
        if (newPwd.equals(confirmPwd)){
            newPwd = md5.getMD5ofStr(newPwd);
            Const.currentuser.setUSER_PWD(newPwd);
            changePwd4Net();
        }else {
            showToast("两次输入的新密码不匹配");
        }
    }

    private void changePwd4Net() {
        DialogUIUtils.showTwoButtonAlertDialog(this, "提示", "是否确认修改密码", "取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {}
        }, "修改", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserToSever(Const.currentuser);
                clearInput();
                DialogUIUtils.showOnlyOneButtonAlertDialog(ChangePasswordActivity.this, "请重新登陆", "去登陆", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Const.currentuser = null;
                        GeneralUtil.startActivity(ChangePasswordActivity.this, LoginActivity.class);
                    }
                });
            }
        }, false);
    }

    private void clearInput() {
        edtUpwd.setText("");
        edtNewPwd.setText("");
        edtPwdConfirm.setText("");
    }

    public void updateUserToSever(final Users user) {
        final String userString = new Gson().toJson(user);
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpUtils.postString().url(Const.URL_UPDATEUSER)
                            .content(userString)
                            .build().execute(new Callback() {
                        @Override
                        public Object parseNetworkResponse(Response response) throws IOException {
                            final String s = response.body().string();
                            if (s.equals("failed")) {
                                showToast("修改失败,服务器故障");
                            } else {
                                Gson gson = new Gson();
                                Const.currentuser = gson.fromJson(s, Users.class);

                                showToast("修改成功");
                            }
                            return null;
                        }

                        @Override
                        public void onError(Request request, Exception e) {
                            showToast("修改失败");
                            Log.e(e.getMessage());
                        }

                        @Override
                        public void onResponse(Object response) {
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(e.getMessage());
                }
            }
        });
        thread.start();
    }
}
