package com.haichaoaixuexi.railway_system_android.app;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haichaoaixuexi.railway_system_android.R;
import com.haichaoaixuexi.railway_system_android.adapter.Myadapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DataSyncActivity extends BaseActivity {


    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.list_data_sync)
    RecyclerView listDataSync;
    Myadapter mAdapter = null;
    List<String> lists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_data_sync);
        ButterKnife.bind(this);
        titleTxt.setText(R.string.data_sync);
    }

    @Override
    protected void initData() {
        lists.add("故障类型下载");
        lists.add("设备信息下载");
        lists.add("设备检修问题上传");
        lists.add("设备维修结果上传");
        //工长分配任务
        /*if (Const.currentuser.getROLE_ID()==2)
            lists.add("本组工人信息同步");*/
        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new Myadapter(R.layout.item_txt,lists);//可以直接传入数据，数据未获取到的情况下可以传null
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);//设置列表加载动画
        mAdapter.isFirstOnly(false);//是否仅在第一次加载列表时展示动画
        //创建布局管理
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listDataSync.setLayoutManager(layoutManager);
        listDataSync.setAdapter(mAdapter);
        //适配器Item点击事件
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_item_click:
                        //showToast("您点击了第" + (position + 1) + "条数据的子view");
                        clickItem(position);
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
        }, listDataSync);
    }

    private void clickItem(int position) {
        switch (position){
            case 0:
                readyGo(EqIssuesActivity.class);
                break;
            case 1:
                readyGo(EqInfoSyncActivity.class);
                break;
        }
    }

    @OnClick(R.id.btn_back)
    public void backToMain() {
        readyGoThenKill(StartActivity.class);
    }
}
