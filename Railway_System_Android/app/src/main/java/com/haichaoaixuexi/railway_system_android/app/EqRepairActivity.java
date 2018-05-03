package com.haichaoaixuexi.railway_system_android.app;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haichaoaixuexi.railway_system_android.R;
import com.haichaoaixuexi.railway_system_android.data.Const;
import com.haichaoaixuexi.railway_system_android.entity.Eq_check;
import com.haichaoaixuexi.railway_system_android.entity.Eq_issue_kind;
import com.haichaoaixuexi.railway_system_android.greendao.Eq_checkDao;
import com.haichaoaixuexi.railway_system_android.greendao.Eq_issue_kindDao;
import com.haichaoaixuexi.railway_system_android.utils.GeneralUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.zwy.kutils.widget.loadingdialog.DialogUIUtils;

import java.sql.Timestamp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by haichao on 2018/2/22.
 * 至尊宝：长夜漫漫无心睡眠，我以为只有我睡不着，原来晶晶姑娘你也睡不着 ！
 * describe: 设备检修信息展示（需网络）
 */
public class EqRepairActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.txt_JCLX)
    TextView txtJCLX;
    @BindView(R.id.setSBBH)
    TextView setSBBH;
    @BindView(R.id.txt_GZLX)
    TextView txtGZLX;
    @BindView(R.id.txt_GZDJ)
    TextView txtGZDJ;
    @BindView(R.id.clip_pic)
    ImageView clipPic;
    @BindView(R.id.txt_GZMS)
    TextView txtGZMS;
    @BindView(R.id.txt_BXR)
    TextView txtBXR;
    @BindView(R.id.txt_BXSJ)
    TextView txtBXSJ;
    @BindView(R.id.btn_qualified)
    TextView btnQualified;
    @BindView(R.id.btn_unqualified)
    TextView btnUnqualified;
    @BindView(R.id.txt_YSJG)
    TextView txtYSJG;
    @BindView(R.id.btn_sync)
    TextView btnSync;
    @BindView(R.id.ll_YSJG)
    LinearLayout llYSJG;
    @BindView(R.id.txt_YSSJ)
    TextView txtYSSJ;
    @BindView(R.id.ll_YSSJ)
    LinearLayout llYSSJ;
    private Appliaction myApp;
    private Eq_checkDao eq_checkDao;
    private Eq_issue_kindDao dao;
    private Eq_issue_kind issue;
    private Eq_check check = new Eq_check();
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_eq_repair_info);
        ButterKnife.bind(this);
        titleTxt.setText("设备检修结果验收");
    }

    @Override
    protected void initData() {
        myApp = (Appliaction) getApplication();
        dao = myApp.getDaoSession().getEq_issue_kindDao();
        eq_checkDao = myApp.getDaoSession().getEq_checkDao();
        final Intent intent = getIntent();
        //获取检修数据
        String checkString = intent.getStringExtra("check");
        //判定跳转此页面的上一个页面
        String src = intent.getStringExtra("src");

        check = gson.fromJson(checkString, Eq_check.class);

        txtJCLX.setText(check.getJCLX());
        setSBBH.setText(check.getSBBH() + "");
        issue = dao.queryBuilder().where(Eq_issue_kindDao.Properties.EQ_ISSUE_KIND.eq(check.getGZLX())).unique();
        if (issue != null)
            txtGZLX.setText(issue.getEQ_ISSUE());
        txtGZDJ.setText(check.getABC());
        try {
            clipPic.setImageBitmap(GeneralUtil.generateImage(Const.imgString));
        } catch (Exception e) {
            showToast(e.getMessage().toString());
        }
        txtGZMS.setText(check.getGZMS());
        txtBXSJ.setText(check.getBXSJ());
        txtBXR.setText(check.getBXR_NAME());

        //设置控件可见度
        if (src.equals("net")) {
            btnQualified.setVisibility(View.VISIBLE);
            btnUnqualified.setVisibility(View.VISIBLE);
            btnSync.setVisibility(View.GONE);
            llYSJG.setVisibility(View.GONE);
            check.setImgString(Const.imgString);
        } else if (src.equals("local")) {
            btnQualified.setVisibility(View.GONE);
            btnUnqualified.setVisibility(View.GONE);
            btnSync.setVisibility(View.VISIBLE);
            llYSJG.setVisibility(View.VISIBLE);
            txtYSJG.setText(check.getYSJG().toString());
        } else if (src.equals("history")) {
            btnQualified.setVisibility(View.GONE);
            btnUnqualified.setVisibility(View.GONE);
            btnSync.setVisibility(View.GONE);
            llYSJG.setVisibility(View.VISIBLE);
            llYSSJ.setVisibility(View.VISIBLE);
            txtYSJG.setText(check.getYSJG().toString().trim());
            txtYSSJ.setText(check.getYSSJ().toString().trim());
        }
    }

    @OnClick(R.id.btn_back)
    public void back() {
        mContext.finish();
    }

    @OnClick(R.id.btn_qualified)
    public void qualified() {
        check.setYSJG("合格");
        showDialog();
    }

    @OnClick(R.id.btn_unqualified)
    public void unqualified() {
        check.setYSJG("不合格");
        showDialog();
    }

    private void showDialog() {
        check.setYSR(Const.currentuser.getUSER_ID());
        check.setYSSJ(new Timestamp(System.currentTimeMillis()).toString());
        DialogUIUtils.showTwoButtonAlertDialog(mContext, "提示", "请选择是否上传", "本地保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        }, "同步上传", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        }, false);
    }

    public void save() {
        eq_checkDao.insert(check);
        DialogUIUtils.showOnlyOneButtonAlertDialog(mContext, "数据保存成功", "确认", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGoThenKill(StartActivity.class);
            }
        });
    }

    @OnClick(R.id.btn_sync)
    public void sync() {
        final Dialog dialog = DialogUIUtils.showLoadingHorizontal(mContext, "数据上传中").show();
        final PostRequest<String> request = OkGo.<String>post(Const.URL_GET_EQ_CHECK)
                .tag(this)
                .params("action", "update")
                .params("YSR", check.getYSR())
                .params("YSSJ", check.getYSSJ())
                .params("YSJG", check.getYSJG())
                .params("ID", check.getEQ_CHECK_ID())
                .params("SBBH", check.getSBBH());
        request.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                if (response.body().trim().equals("success")) {
                    DialogUIUtils.showOnlyOneButtonAlertDialog(mContext, "数据上传成功", "确认", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            eq_checkDao.deleteByKey(check.getId());
                            readyGoThenKill(StartActivity.class);
                        }
                    });
                } else {
                    showToast(response.body());
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dialog.dismiss();
            }
        });
    }

    public void upload() {
        final Dialog dialog = DialogUIUtils.showLoadingHorizontal(mContext, "数据上传中").show();
        final PostRequest<String> request = OkGo.<String>post(Const.URL_GET_EQ_CHECK)
                .tag(this)
                .params("action", "update")
                .params("YSR", check.getYSR())
                .params("YSSJ", check.getYSSJ())
                .params("YSJG", check.getYSJG())
                .params("ID", check.getEQ_CHECK_ID())
                .params("SBBH", check.getSBBH());
        request.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                if (response.body().trim().equals("success")) {
                    DialogUIUtils.showOnlyOneButtonAlertDialog(mContext, "数据上传成功", "确认", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            readyGoThenKill(StartActivity.class);
                        }
                    });
                } else {
                    showToast(response.body());
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dialog.dismiss();
            }
        });
    }
}
