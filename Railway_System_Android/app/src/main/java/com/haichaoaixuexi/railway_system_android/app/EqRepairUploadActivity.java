package com.haichaoaixuexi.railway_system_android.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.haichaoaixuexi.railway_system_android.R;
import com.haichaoaixuexi.railway_system_android.adapter.Myadapter;
import com.haichaoaixuexi.railway_system_android.data.Const;
import com.haichaoaixuexi.railway_system_android.entity.Eq_check;
import com.haichaoaixuexi.railway_system_android.entity.Equipment;
import com.haichaoaixuexi.railway_system_android.greendao.Eq_checkDao;
import com.haichaoaixuexi.railway_system_android.greendao.EquipmentDao;
import com.zwy.kutils.widget.loadingdialog.DialogUIUtils;
import com.zwy.kutils.widget.loadingdialog.alertdialog.ActionSheetDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mob.MobSDK.getContext;

/**
 * Created by haichao on 2018/2/22.
 * 至尊宝：长夜漫漫无心睡眠，我以为只有我睡不着，原来晶晶姑娘你也睡不着 ！
 * describe:本地存储的设备检修信息
 */
public class EqRepairUploadActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.list_eq_check)
    RecyclerView listEqCheck;
    @BindView(R.id.btn_task_sync)
    TextView btnTaskSync;
    @BindView(R.id.btn_task_clear)
    TextView btnTaskClear;
    //手机数据库操作相关
    private Appliaction myApp;
    private Eq_checkDao eq_checkDao;
    private EquipmentDao dao;
    private List<Eq_check> checks = new ArrayList<>();
    //列表显示相关
    private Myadapter mAdapter = null;
    private List<String> lists = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_eq_check_list);
        ButterKnife.bind(this);
        titleTxt.setText(R.string.local_eq_repair);
        btnTaskSync.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        myApp = (Appliaction) getApplication();
        eq_checkDao = myApp.getDaoSession().getEq_checkDao();
        dao = myApp.getDaoSession().getEquipmentDao();
        lists.clear();
        checks = eq_checkDao.queryBuilder().list();
        for (Eq_check check:checks) {
            if (check.getYSR()==0){
                continue;
            }
            Equipment equipment = dao.queryBuilder().where(EquipmentDao.Properties.EQUIP_ID.eq(check.getSBBH())).unique();
            if (equipment==null)
                lists.add(check.getSBBH()+"");
            else {
                lists.add(equipment.getSBMC());
            }
        }
        initAdapter();
    }
    private void initAdapter() {
        mAdapter = new Myadapter(R.layout.item_txt, lists);//可以直接传入数据，数据未获取到的情况下可以传null
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);//设置列表加载动画
        mAdapter.isFirstOnly(false);//是否仅在第一次加载列表时展示动画
        //创建布局管理
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listEqCheck.setLayoutManager(layoutManager);
        listEqCheck.setAdapter(mAdapter);
        //适配器Item点击事件
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_item_click:
                        //showToast("您点击了第" + (position + 1) + "条数据的子view");
                        Intent intent = new Intent(mContext, EqRepairActivity.class);
                        Gson gson = new Gson();
                        //减少intent传输字符串大小
                        Const.imgString = checks.get(position).getImgString();
                        checks.get(position).setImgString("");
                        intent.putExtra("check", gson.toJson(checks.get(position)));
                        intent.putExtra("src", "local");
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
        }, listEqCheck);
    }
    @OnClick(R.id.btn_back)
    public void back() {
        mContext.finish();
    }
    /**
     * 清空任务（本地数据库）
     */
    @OnClick(R.id.btn_task_clear)
    public void clearTask() {
        DialogUIUtils
                .showIsDeleteSheetDialog(mContext, "删除数据，谨慎操作", new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        eq_checkDao.deleteAll();
                        refresh(EqRepairUploadActivity.class);
                        showToast("数据已全部删除");
                    }
                });
    }
}
