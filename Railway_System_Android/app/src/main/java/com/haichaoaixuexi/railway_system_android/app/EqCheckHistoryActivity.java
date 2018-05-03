package com.haichaoaixuexi.railway_system_android.app;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haichaoaixuexi.railway_system_android.R;
import com.haichaoaixuexi.railway_system_android.adapter.Myadapter;
import com.haichaoaixuexi.railway_system_android.data.Const;
import com.haichaoaixuexi.railway_system_android.entity.Eq_check;
import com.haichaoaixuexi.railway_system_android.entity.Eq_issue_kind;
import com.haichaoaixuexi.railway_system_android.greendao.Eq_issue_kindDao;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zwy.kutils.widget.loadingdialog.DialogUIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mob.MobSDK.getContext;

public class EqCheckHistoryActivity extends BaseActivity {

    @BindView(R.id.list_ec_history)
    RecyclerView listEcHistory;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.title_txt)
    TextView titleTxt;
    private Appliaction myApp;
    private Eq_issue_kindDao dao;
    private Eq_issue_kind issue;
    Gson gson =new Gson();
    //列表显示相关
    private Myadapter mAdapter = null;
    private List<String> lists = new ArrayList<>();
    private List<Eq_check> ecs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_eq_check_history);
        ButterKnife.bind(this);
        titleTxt.setText("设备检修历史");
    }

    @Override
    protected void initData() {
        myApp = (Appliaction) getApplication();
        dao = myApp.getDaoSession().getEq_issue_kindDao();
        lists.clear();
        String SBBH = getIntent().getStringExtra("SBBH");
        downloadFromSever(Integer.parseInt(SBBH));
    }

    private void downloadFromSever(int SBBH) {
        final Dialog dialog = DialogUIUtils.showLoadingHorizontal(mContext, "数据加载中").show();
        OkGo.<String>post(Const.URL_GET_EQ_CHECK)
                .tag(this)
                .params("action", "getAllBySBBH")
                .params("SBBH", SBBH)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.body().equals("failed")) {
                            Toast.makeText(mContext, "服务器异常",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Gson gson = new Gson();
                            ecs = gson.fromJson(response.body(), new TypeToken<List<Eq_check>>() {
                            }.getType());
                            for (Eq_check ec : ecs) {
                                issue = dao.queryBuilder().where(Eq_issue_kindDao.Properties.EQ_ISSUE_KIND.eq(ec.getGZLX())).unique();
                                if (issue != null)
                                    lists.add(ec.getABC() + "级-" + ec.getJCLX() + "-" + issue.getEQ_ISSUE());
                                else
                                    lists.add(ec.getABC() + "级-" + ec.getJCLX() + "-未知故障-" + ec.getGZLX());
                            }
                            initAdapter();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Toast.makeText(mContext, "请检查网络",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dialog.dismiss();
                    }
                });
    }

    private void initAdapter() {
        mAdapter = new Myadapter(R.layout.item_txt, lists);//可以直接传入数据，数据未获取到的情况下可以传null
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);//设置列表加载动画
        mAdapter.isFirstOnly(false);//是否仅在第一次加载列表时展示动画
        //创建布局管理
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listEcHistory.setLayoutManager(layoutManager);
        listEcHistory.setAdapter(mAdapter);
        //适配器Item点击事件
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_item_click:
                        Intent intent = new Intent(mContext, EqRepairActivity.class);
                        //减少intent传输字符串大小
                        Const.imgString = ecs.get(position).getImgString();
                        ecs.get(position).setImgString("");
                        intent.putExtra("check", gson.toJson(ecs.get(position)));
                        intent.putExtra("src", "history");
                        startActivity(intent);
                        break;
                }
            }
        });
        //加载更多监听
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mAdapter.loadMoreEnd();
            }
        }, listEcHistory);
    }
    @OnClick(R.id.btn_back)
    public void back() {
        mContext.finish();
    }
}
