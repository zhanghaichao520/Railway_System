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
import com.haichaoaixuexi.railway_system_android.entity.Feedback;
import com.haichaoaixuexi.railway_system_android.utils.GeneralUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zwy.kutils.widget.loadingdialog.DialogUIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by haichao on 2018/3/6.
 * 至尊宝：长夜漫漫无心睡眠，我以为只有我睡不着，原来晶晶姑娘你也睡不着 ！
 */

public class FeedBackActivity extends BaseActivity {
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.feedBackText)
    EditText feedBackText;
    @BindView(R.id.btn_feedback)
    Button btnFeedback;
    String content;
    @BindView(R.id.btn_cleartInput)
    Button btnCleartInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        titleTxt.setText(R.string.feedBack);

    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.btn_back)
    public void backToMain() {
        readyGoThenKill(StartActivity.class);
    }

    @OnClick(R.id.btn_feedback)
    public void feedBack() {
        content = feedBackText.getText().toString().trim();
        if (content == null || content.equals("")) {
            showToast("输入不能为空");
            return;
        }
        Feedback fb = new Feedback();
        fb.setCONTENT(content);
        fb.setFKR(Const.currentuser.getUSER_ID());
        Gson gson = new Gson();
        final Dialog dialog = DialogUIUtils.showLoadingHorizontal(FeedBackActivity.this, "请等待").show();
        OkGo.<String>post(Const.URL_FEEDBACK)
                .tag(this)
                .upJson(gson.toJson(fb))
                .execute(new StringCallback() {
                    String suggest = "";
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.body().toString().trim().equals("success")) {
                            suggest = "反馈成功，感谢您的宝贵意见";
                        } else {
                            suggest = "反馈失败，服务器故障";
                        }
                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        suggest = "反馈失败，请检测网络";

                    }
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        //为了多显示一会漂亮的加载框
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                        feedBackText.setText("");
                        DialogUIUtils.showOnlyOneButtonAlertDialog(FeedBackActivity.this, suggest, "确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                GeneralUtil.startActivity(FeedBackActivity.this, StartActivity.class);
                            }
                        });
                    }
                });
    }
    @OnClick(R.id.btn_cleartInput)
    public void clearInput(){
        feedBackText.setText("");
    }
}
