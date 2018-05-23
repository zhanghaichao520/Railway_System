package com.haichaoaixuexi.railway_system_android.app;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haichaoaixuexi.railway_system_android.R;
import com.haichaoaixuexi.railway_system_android.data.Const;
import com.haichaoaixuexi.railway_system_android.entity.Eq_analysis;
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
import butterknife.OnClick;

public class AnalysisActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.table)
    SmartTable table;
    @BindView(R.id.txt_time)
    TextView txtTime;
    //服务端读取对象
    private List<Eq_check> ecs = new ArrayList<>();
    //显示对象列表
    private List<Eq_analysis> list = new ArrayList<>();
    private Appliaction myApp;
    private EquipmentDao dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_analysis);
        ButterKnife.bind(this);
        titleTxt.setText(R.string.analysis);
    }

    @Override
    protected void initData() {
        myApp = (Appliaction) getApplication();
        dao = myApp.getDaoSession().getEquipmentDao();
        list.clear();
        String time = ""+getIntent().getStringExtra("time");
        txtTime.setText(time + " 后设备检修统计分析表");
        downloadFromSever(time);


    }
    private void loadTable() {
        if (list!=null){
            table.setData(list);
        }
        table.getConfig().setContentBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.position % 2 == 0)
                    return ContextCompat.getColor(mContext, R.color.bg_gray);
                else
                    return ContextCompat.getColor(mContext, R.color.white);
            }
        });
        table.setZoom(true);
        table.getConfig().setShowXSequence(false).setShowYSequence(false).setMinTableWidth(100);
    }
    private void downloadFromSever(String time) {
        final Dialog dialog = DialogUIUtils.showLoadingHorizontal(mContext, "数据加载中").show();
        OkGo.<String>post(Const.URL_GET_EQ_CHECK)
                .tag(this)
                .params("action", "getAllByYSSJ")
                .params("YSSJ", time)
                .params("gid",Const.currentuser.getGROUP_ID())
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
                            //遍历每一条记录
                            for (Eq_check ec : ecs) {
                                Equipment equipment = dao.queryBuilder().where(EquipmentDao.Properties.EQUIP_ID.eq(ec.getSBBH())).unique();
                                boolean flag = false;
                                if (equipment==null){
                                    showToast("请同步设备字典");
                                    readyGoThenKill(DataSyncActivity.class);
                                    break;
                                }
                                //首先判断设备是否存在
                                for (int i= 0; i < list.size();i ++){
                                    Eq_analysis eqa = list.get(i);
                                    if (ec.getSBBH()==eqa.getSBBH()){
                                        flag = true;
                                        List<Eq_check> temp = eqa.getEcs();
                                        temp.add(ec);
                                        eqa.setEcs(temp);
                                        eqa.setCount(eqa.getCount()+1);
                                        eqa.setRatio(eqa.getCount()*100/ecs.size());
                                        if (ec.getYSJG().equals("合格")){
                                            eqa.setPassCount(eqa.getPassCount()+1);
                                        }
                                        eqa.setPassPercent(eqa.getPassCount()*100/temp.size());
                                    }
                                }
                                //如果不存在,创建新对象，加入list
                                if (!flag){
                                    Eq_analysis eqa = new Eq_analysis();
                                    //将记录保存
                                    List<Eq_check> temp = eqa.getEcs();
                                    temp.add(ec);
                                    eqa.setEcs(temp);
                                    eqa.setSBBH(ec.getSBBH());
                                    eqa.setSBMC(equipment.getSBMC());
                                    eqa.setCount(1);
                                    eqa.setCycle(equipment.getXXZQ());
                                    eqa.setRatio(eqa.getCount()*100/ecs.size());
                                    if (ec.getYSJG().equals("合格")){
                                        eqa.setPassCount(1);
                                    }
                                    eqa.setPassPercent(eqa.getPassCount()*100/temp.size());
                                    list.add(eqa);
                                }
                            }
                            loadTable();
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
    @OnClick(R.id.btn_back)
    public void backToMain() {
        mContext.finish();
    }
}
