package com.haichaoaixuexi.railway_system_android.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haichaoaixuexi.railway_system_android.R;
import com.haichaoaixuexi.railway_system_android.adapter.Myadapter;
import com.haichaoaixuexi.railway_system_android.app.Appliaction;
import com.haichaoaixuexi.railway_system_android.app.EqRepairActivity;
import com.haichaoaixuexi.railway_system_android.data.Const;
import com.haichaoaixuexi.railway_system_android.entity.Eq_check;
import com.haichaoaixuexi.railway_system_android.entity.Equipment;
import com.haichaoaixuexi.railway_system_android.greendao.EquipmentDao;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zwy.kutils.widget.loadingdialog.DialogUIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.mob.tools.utils.DeviceHelper.getApplication;

public class LogFragment extends Fragment {

    @BindView(R.id.list_log)
    RecyclerView listLog;
    Unbinder unbinder;
    @BindView(R.id.get_log)
    TextView getLog;
    private Appliaction myApp;
    private EquipmentDao dao;
    private Equipment equipment;
    //列表显示相关
    private Myadapter mAdapter = null;
    private List<String> lists = new ArrayList<>();
    private List<Eq_check> ecs = new ArrayList<>();
    private List<Eq_check> ecs2 = new ArrayList<>();
    Gson gson = new Gson();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_log, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myApp = (Appliaction) getApplication();
        dao = myApp.getDaoSession().getEquipmentDao();
        lists.clear();
        if (Const.currentuser!=null){
            downloadFromSever(Const.currentuser.getUSER_ID());
        }

    }

    private void downloadFromSever(final int USER_ID) {
        final Dialog dialog = DialogUIUtils.showLoadingHorizontal(getActivity(), "数据加载中").show();
        OkGo.<String>post(Const.URL_GET_EQ_CHECK)
                .tag(this)
                .params("action", "getAllByUSER")
                .params("USER_ID", USER_ID)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.body().equals("sever failed")) {
                            Toast.makeText(getActivity(), "服务器异常",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            ecs = gson.fromJson(response.body(), new TypeToken<List<Eq_check>>() {
                            }.getType());
                            for (Eq_check ec : ecs) {
                                equipment = dao.queryBuilder().where(EquipmentDao.Properties.EQUIP_ID.eq(ec.getSBBH())).unique();
                                if (equipment != null) {
                                    if (ec.getBXR() == USER_ID){
                                        lists.add(ec.getJCLX() + "-" + equipment.getSBMC());
                                        ecs2.add(ec);
                                    }

                                    if (ec.getYSR() == USER_ID){
                                        lists.add("审核" + "-" + equipment.getSBMC());
                                        ecs2.add(ec);
                                    }
                                } else {
                                    if (ec.getBXR() == USER_ID){
                                        lists.add(ec.getJCLX() + "-" + ec.getSBBH());
                                        ecs2.add(ec);
                                    }
                                    if (ec.getYSR() == USER_ID){
                                        lists.add("审核" + "-" + ec.getSBBH());
                                        ecs2.add(ec);
                                    }
                                }
                            }

                            initAdapter();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Toast.makeText(getActivity(), "请检查网络",
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
        listLog.setLayoutManager(layoutManager);
        listLog.setAdapter(mAdapter);
        //适配器Item点击事件
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_item_click:
                        Intent intent = new Intent(getActivity(), EqRepairActivity.class);
                        //减少intent传输字符串大小
                        Const.imgString = ecs2.get(position).getImgString();
                        ecs2.get(position).setImgString("");
                        intent.putExtra("check", gson.toJson(ecs2.get(position)));
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
        }, listLog);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
