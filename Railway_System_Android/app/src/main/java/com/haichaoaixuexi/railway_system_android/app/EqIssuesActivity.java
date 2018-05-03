package com.haichaoaixuexi.railway_system_android.app;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haichaoaixuexi.railway_system_android.R;
import com.haichaoaixuexi.railway_system_android.data.Const;
import com.haichaoaixuexi.railway_system_android.entity.Eq_issue_kind;
import com.haichaoaixuexi.railway_system_android.greendao.Eq_issue_kindDao;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zwy.kutils.widget.loadingdialog.DialogUIUtils;
import com.zwy.kutils.widget.loadingdialog.alertdialog.ActionSheetDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by haichao on 2018/2/22.
 * 至尊宝：长夜漫漫无心睡眠，我以为只有我睡不着，原来晶晶姑娘你也睡不着 ！
 */
public class EqIssuesActivity extends BaseActivity {
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.table)
    SmartTable table;
    @BindView(R.id.btn_data_sync)
    TextView btnDataSync;
    @BindView(R.id.btn_data_clear)
    TextView btnDataClear;
    private Appliaction myApp;
    private Eq_issue_kindDao dao;
    private List<Eq_issue_kind> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_sync_display);
        ButterKnife.bind(this);
        titleTxt.setText(R.string.eq_issues_sync);
    }

    @Override
    protected void initData() {
        myApp = (Appliaction) getApplication();
        dao = myApp.getDaoSession().getEq_issue_kindDao();
        loadTable();
    }

    private void loadTable() {
        list = dao.queryBuilder().list();
        if (list!=null){
            table.setData(list);
        }
        table.getConfig().setContentBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.position % 2 == 0)
                    return ContextCompat.getColor(EqIssuesActivity.this, R.color.bg_gray);
                else
                    return ContextCompat.getColor(EqIssuesActivity.this, R.color.white);
            }
        });
        table.setZoom(true);
        table.getConfig().setShowXSequence(false).setShowYSequence(false).setMinTableWidth(100);

    }

    @OnClick(R.id.btn_back)
    public void backToMain() {
        readyGoThenKill(DataSyncActivity.class);
    }

    @OnClick(R.id.btn_data_sync)
    public void download_issue() {
        DialogUIUtils.showTwoButtonAlertDialog(mContext, "提示", "请确保网络畅通", "取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }, "同步", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download();
            }
        }, false);

    }

    private void download() {
        final Dialog dialog = DialogUIUtils.showLoadingHorizontal(EqIssuesActivity.this, "数据下载中").show();
        OkGo.<String>post(Const.URL_DATA_SYNC).tag(this).params("action", "eq_issue_kind").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                if (response.body().equals("failed")) {
                    Message message = Message.obtain();
                    message.what = 1;
                    myHandler.sendMessage(message);
                } else {
                    dao.deleteAll();
                    Gson gson = new Gson();
                    List<Eq_issue_kind> issues = gson.fromJson(response.body(), new TypeToken<List<Eq_issue_kind>>() {
                    }.getType());
                    for (Eq_issue_kind issue : issues) {
                        dao.insert(issue);
                    }
                    Message message = Message.obtain();
                    message.what = 0;
                    myHandler.sendMessage(message);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Message message = Message.obtain();
                message.what = 1;
                myHandler.sendMessage(message);
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
            }
        });
    }

    @OnClick(R.id.btn_data_clear)
    public void clearData() {
        DialogUIUtils.showIsDeleteSheetDialog(mContext, "删除数据，谨慎操作", new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                dao.deleteAll();
                refresh(EqIssuesActivity.class);
                showToast("数据已全部删除");
            }
        });
    }

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    refresh(EqIssuesActivity.class);
                    DialogUIUtils.showToast("设备故障信息同步成功");
                    break;
                case 1:
                    DialogUIUtils.showToast("设备故障信息同步失败");
                    break;
                default:
                    break;
            }
        }
    };
}
