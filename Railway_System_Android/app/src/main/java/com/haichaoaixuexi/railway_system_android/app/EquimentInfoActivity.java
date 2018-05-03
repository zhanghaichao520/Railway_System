package com.haichaoaixuexi.railway_system_android.app;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haichaoaixuexi.railway_system_android.R;
import com.haichaoaixuexi.railway_system_android.data.Const;
import com.haichaoaixuexi.railway_system_android.entity.Equipment;
import com.haichaoaixuexi.railway_system_android.greendao.EquipmentDao;
import com.haichaoaixuexi.railway_system_android.utils.GeneralUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zwy.kutils.widget.loadingdialog.DialogUIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EquimentInfoActivity extends BaseActivity {

    String SBBH = "";
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.setSBBH)
    TextView setSBBH;
    @BindView(R.id.setSSQY)
    TextView setSSQY;
    @BindView(R.id.setSBMC)
    TextView setSBMC;
    @BindView(R.id.setSBGG)
    TextView setSBGG;
    @BindView(R.id.setZZCJ)
    TextView setZZCJ;
    @BindView(R.id.setZZRQ)
    TextView setZZRQ;
    @BindView(R.id.setSBZT)
    TextView setSBZT;
    @BindView(R.id.setSSCJ)
    TextView setSSCJ;
    @BindView(R.id.setXXZQ)
    TextView setXXZQ;
    @BindView(R.id.btn_repair)
    TextView btnRepair;
    @BindView(R.id.btn_history)
    TextView btnHistory;
    private Appliaction myApp;
    private EquipmentDao dao;
    private Equipment equipment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_equiment_info);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        final Intent intent = getIntent();
        SBBH = intent.getStringExtra("SBBH");
        titleTxt.setText(R.string.equi_info);

        myApp = (Appliaction) getApplication();
        dao = myApp.getDaoSession().getEquipmentDao();
        equipment = dao.queryBuilder().where(EquipmentDao.Properties.EQUIP_ID.eq(SBBH)).unique();
        if (equipment != null) {
            setSBBH.setText(SBBH);
            setSSQY.setText(equipment.getSSQY());
            setSBMC.setText(equipment.getSBMC());
            setSBGG.setText(equipment.getSBGG());
            setZZCJ.setText(equipment.getZZCJ());
            setZZRQ.setText(equipment.getZZRQ());
            setSBZT.setText(equipment.getCONTENT());
            setSSCJ.setText(equipment.getGROUP_NAME());
            setXXZQ.setText(equipment.getXXZQ() + " 天 ");
        } else {
            DialogUIUtils.showTwoButtonAlertDialog(this, "提示", "设备字典中暂时没有该设备", "取消", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    readyGo(StartActivity.class);
                }
            }, "网络查找", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    download();
                }
            }, false);

        }

    }

    @OnClick(R.id.btn_back)
    public void backToMain() {
        readyGoThenKill(StartActivity.class);
    }

    private void download() {
        final Dialog dialog = DialogUIUtils.showLoadingHorizontal(EquimentInfoActivity.this, "数据下载中").show();
        OkGo.<String>post(Const.URL_GET_EQUIPMENT).tag(this)
                .params("action", "byid")
                .params("id", SBBH)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.body().equals("failed")) {
                            DialogUIUtils.showOnlyOneButtonAlertDialog(EquimentInfoActivity.this, "下载失败，服务器无该设备信息", "返回", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    GeneralUtil.startActivity(EquimentInfoActivity.this, StartActivity.class);
                                }
                            });
                        } else if (response.body().equals("failed_not_allow")) {
                            DialogUIUtils.showOnlyOneButtonAlertDialog(EquimentInfoActivity.this, "下载失败,权限不足", "返回", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    GeneralUtil.startActivity(EquimentInfoActivity.this, StartActivity.class);
                                }
                            });
                        } else {
                            Gson gson = new Gson();
                            Equipment equipment = gson.fromJson(response.body(), Equipment.class);
                            if (equipment.getGROUP_ID() == Const.currentuser.getGROUP_ID()) {
                                dao.insert(equipment);
                                setSBBH.setText(SBBH);
                                setSSQY.setText(equipment.getSSQY());
                                setSBMC.setText(equipment.getSBMC());
                                setSBGG.setText(equipment.getSBGG());
                                setZZCJ.setText(equipment.getZZCJ());
                                setZZRQ.setText(equipment.getZZRQ());
                                setSBZT.setText(equipment.getCONTENT());
                                setSSCJ.setText(equipment.getGROUP_NAME());
                                setXXZQ.setText(equipment.getXXZQ() + " 天 ");
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        DialogUIUtils.showOnlyOneButtonAlertDialog(EquimentInfoActivity.this, "下载失败,未知错误", "返回", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                GeneralUtil.startActivity(EquimentInfoActivity.this, StartActivity.class);
                            }
                        });
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
    @OnClick(R.id.btn_repair)
    public void repair(){
        Bundle bundle =new Bundle();
        bundle.putString("SBBH",SBBH);
        if (equipment != null) {
            bundle.putString("GZLX",equipment.getGZLX());
        }
        if (equipment.getSBZT()!=-1){
            readyGo(EqCheckInfoActivity.class,bundle);
        }
        else {
            showToast("设备未投入使用");
        }
    }
}
