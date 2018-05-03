package com.haichaoaixuexi.railway_system_android.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haichaoaixuexi.railway_system_android.R;
import com.haichaoaixuexi.railway_system_android.app.LoginActivity;
import com.haichaoaixuexi.railway_system_android.data.Const;
import com.haichaoaixuexi.railway_system_android.entity.Users;
import com.haichaoaixuexi.railway_system_android.utils.GeneralUtil;
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
import butterknife.Unbinder;

import static android.view.View.VISIBLE;
import static com.zwy.kutils.widget.loadingdialog.DialogUIUtils.showToast;

/**
 * Created by haichaoaixuexi on 2018/2/12.
 */

public class PersonalFragment extends Fragment {


    @BindView(R.id.txt_setName)
    TextView txtSetName;
    @BindView(R.id.setNum)
    TextView setNum;
    @BindView(R.id.btn_setPhone)
    TextView btnSetPhone;
    @BindView(R.id.setPhone)
    TextView setPhone;
    @BindView(R.id.btn_setAddr)
    TextView btnSetAddr;
    @BindView(R.id.setAddr)
    TextView setAddr;
    @BindView(R.id.setPosition)
    TextView setPosition;
    @BindView(R.id.setGroup)
    TextView setGroup;
    @BindView(R.id.versionName)
    TextView versionName;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_personal, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (Const.currentuser != null) {
            btnLogout.setVisibility(VISIBLE);
            setInfo();
        } else {
            btnLogout.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    private void setInfo() {
        setNum.setText(Const.currentuser.getUSER_NUM().toString().trim());
        setPhone.setText(Const.currentuser.getUSER_TEL().toString().trim());
        setAddr.setText(Const.currentuser.getUSER_ADDR().toString().trim());
        setPosition.setText(Const.currentuser.getUSER_ROLE().toString().trim());
        setGroup.setText(Const.currentuser.getGROUP_NAME().toString().trim());
        txtSetName.setText(Const.currentuser.getUSER_NAME().toString().trim());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_setPhone)
    public void updatePhone() {
        if (Const.currentuser == null) {
            DialogUIUtils.showToastCenter("请先登陆");
            return;
        }
        final EditText inputServer = new EditText(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("更改手机").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer).setNegativeButton("取消",
                null);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String phone = inputServer.getText().toString().trim();
                if (GeneralUtil.isMobiPhoneNum(phone)) {
                    Users user = Const.currentuser;
                    user.setUSER_TEL(phone);
                    updateUserToSever(user);
                } else {
                    GeneralUtil.toast(getContext(), "请输入合法的手机号");
                }
            }
        });
        builder.show();
    }

    @OnClick(R.id.btn_setAddr)
    public void updateAddr() {
        if (Const.currentuser == null) {
            DialogUIUtils.showToastCenter("请先登陆");
            return;
        }
        final EditText inputServer = new EditText(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("更改地址").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer).setNegativeButton("取消",
                null);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String addr = inputServer.getText().toString().trim();
                Users user = Const.currentuser;
                user.setUSER_ADDR(addr);
                updateUserToSever(user);
            }
        });
        builder.show();
    }

    @OnClick(R.id.btn_logout)
    public void logout() {
        DialogUIUtils.showTwoButtonAlertDialog(getActivity(), "提示", "是否注销用户", "取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }, "注销", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Const.currentuser = null;
                GeneralUtil.startActivity(getActivity(), LoginActivity.class);
                showToast("注销成功");
            }
        }, false);

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
                                Message message = Message.obtain();
                                message.what = 1;
                                myHandler.sendMessage(message);
                            } else {
                                Message message = Message.obtain();
                                message.what = 0;
                                Bundle bundle = new Bundle();
                                bundle.putString("json", s);
                                message.setData(bundle);
                                myHandler.sendMessage(message);
                            }
                            return null;
                        }

                        @Override
                        public void onError(Request request, Exception e) {
                            Message message = Message.obtain();
                            message.what = 1;
                            myHandler.sendMessage(message);
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

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Bundle bundle = msg.getData();
                    Gson gson = new Gson();
                    Const.currentuser = gson.fromJson(bundle.getString("json"), Users.class);
                    Log.e(bundle.getString("json"));
                    setPhone.setText(Const.currentuser.getUSER_TEL().toString().trim());
                    setAddr.setText(Const.currentuser.getUSER_ADDR().toString().trim());
                    showToast("修改成功");
                    break;
                case 1:
                    showToast("修改失败");
                    break;
                default:
                    break;
            }
        }
    };
}
