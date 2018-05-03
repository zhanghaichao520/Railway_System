package com.haichaoaixuexi.railway_system_android.app;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haichaoaixuexi.railway_system_android.R;
import com.haichaoaixuexi.railway_system_android.adapter.Myadapter;
import com.haichaoaixuexi.railway_system_android.data.Const;
import com.haichaoaixuexi.railway_system_android.entity.Task;
import com.haichaoaixuexi.railway_system_android.entity.Users;
import com.haichaoaixuexi.railway_system_android.greendao.TaskDao;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;
import com.zwy.kutils.widget.loadingdialog.DialogUIUtils;
import com.zwy.kutils.widget.loadingdialog.alertdialog.ActionSheetDialog;
import com.zwy.kutils.widget.loadingdialog.bean.TieBean;
import com.zwy.kutils.widget.loadingdialog.listener.DialogUIItemListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.haichaoaixuexi.railway_system_android.data.Const.REQUEST_CODE_SCAN;

public class EqCheckListActivity extends BaseActivity {

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
    private TaskDao dao;
    private List<Task> tasks = new ArrayList<>();
    //列表显示相关
    private Myadapter mAdapter = null;
    private List<String> lists = new ArrayList<>();
    private List<TieBean> userList = new ArrayList<>();
    String[] strs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_eq_check_list);
        ButterKnife.bind(this);
        titleTxt.setText(R.string.eq_check_list);
    }

    @Override
    protected void initData() {
        myApp = (Appliaction) getApplication();
        dao = myApp.getDaoSession().getTaskDao();
        loadDataFromLocal();
        initAdapter();
        if (Const.currentuser.getROLE_ID() == 2)
            getUserInMyGroup();
    }

    /**
     * 加载本地数据库中任务表中的数据
     */
    private void loadDataFromLocal() {
        tasks = dao.queryBuilder().list();
        if (tasks != null) {
            for (int i = 0; i < tasks.size(); i++) {
                lists.add(tasks.get(i).getABC() + "级: " + tasks.get(i).getSBBH());
            }
        }

    }

    private void initAdapter() {
        mAdapter = new Myadapter(R.layout.item_eq_check, lists);//可以直接传入数据，数据未获取到的情况下可以传null
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);//设置列表加载动画
        mAdapter.isFirstOnly(false);//是否仅在第一次加载列表时展示动画
        //创建布局管理
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listEqCheck.setLayoutManager(layoutManager);
        listEqCheck.setAdapter(mAdapter);
        //适配器Item点击事件
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_item_click:
                        if (Const.currentuser.getROLE_ID() == 2)
                            divTask(position);
                        else
                            showToast("权限不足");
                        break;
                    case R.id.tv_item_title:
                        strs = lists.get(position).split(":");
                        checkEq();
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

    /**
     * 通过扫描设备标签，核实设备
     */
    private void checkEq() {
        Intent intent = new Intent(mContext, CaptureActivity.class);
         /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
         * 也可以不传这个参数
         * 不传的话  默认都为默认不震动  其他都为true
         * */

        ZxingConfig config = new ZxingConfig();
        //config.setShowbottomLayout(true);//底部布局（包括闪光灯和相册）
        config.setPlayBeep(true);//是否播放提示音
        config.setShake(true);//是否震动
        config.setShowAlbum(true);//是否显示相册
        config.setShowFlashLight(true);//是否显示闪光灯
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(intent, REQUEST_CODE_SCAN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                //二维码内容
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                //取出扫描结果
                char type = content.charAt(0);
                content = content.substring(1);
                if (type==Const.TYPE_EQUIMENT&&content.length()==9){
                    //将设备编号从列表中提取，并传递到设备显示页面
                    if (strs[1].trim().equals(content)){
                        Intent intent = new Intent(EqCheckListActivity.this, EquimentInfoActivity.class);
                        intent.putExtra("SBBH", strs[1].trim());
                        startActivity(intent);
                    }else {
                        showToast("设备不匹配");
                    }
                }else {
                    DialogUIUtils.showToast("二维码类别不正确");
                }
            }
        }
    }

    /**
     * 分配任务
     *
     * @param pos 任务在tasks列表中的位置
     */
    private void divTask(final int pos) {
        DialogUIUtils.showCenterSheet(this, userList, true, false, new DialogUIItemListener() {
            @Override
            public void onItemClick(CharSequence text, int position) {
                final int task_id = tasks.get(pos).getTASK_ID();
                final int user_id = userList.get(position).getId();
                DialogUIUtils.showTwoButtonAlertDialog(mContext, "提示", "确定将此任务分配给" + userList.get(position).getTitle() + "吗", "取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }, "分配", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        divTask4Sever(task_id, user_id);
                    }
                }, false);
            }
        }).show();
    }

    private void divTask4Sever(int tid, int uid) {
        final Dialog dialog = DialogUIUtils.showLoadingHorizontal(EqCheckListActivity.this, "数据下载中").show();
        OkGo.<String>post(Const.URL_TASK_DIVIDE).tag(this)
                .params("action", "divTask")
                .params("uid", uid)
                .params("tid", tid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.body().equals("failed")) {
                            DialogUIUtils.showToast("服务器故障");
                        } else {
                            download();
                            DialogUIUtils.showToast("分配成功");
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        DialogUIUtils.showToast("分配失败");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dialog.dismiss();
                    }
                });
    }

    /**
     * 得到本组员工信息，用于分配任务
     */
    private void getUserInMyGroup() {
        OkGo.<String>post(Const.URL_GET_USER_BY_GROUP)
                .tag(this)
                .params("gid", Const.currentuser.getGROUP_ID())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        userList.clear();
                        Gson gson = new Gson();
                        List<Users> users = gson.fromJson(response.body(), new TypeToken<List<Users>>() {
                        }.getType());
                        for (int i = 0; i < users.size(); i++) {
                            Users u = users.get(i);
                            //除去自己
                            if (u.getUSER_ID() != Const.currentuser.getUSER_ID()) {
                                TieBean user = new TieBean(u.getUSER_NAME(), u.getUSER_ID());
                                userList.add(user);
                            }
                        }
                    }
                });
    }

    @OnClick(R.id.btn_back)
    public void backToMain() {
        readyGoThenKill(StartActivity.class);
    }

    @OnClick(R.id.btn_task_sync)
    public void download_task() {
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

    /**
     * 从服务器下载任务
     */
    private void download() {
        final Dialog dialog = DialogUIUtils.showLoadingHorizontal(EqCheckListActivity.this, "数据下载中").show();
        OkGo.<String>post(Const.URL_TASK_DIVIDE).tag(this)
                .params("action", "getTask")
                .params("uid", Const.currentuser.getUSER_ID())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.body().equals("failed")) {
                            DialogUIUtils.showToast("服务器故障");
                        } else {
                            dao.deleteAll();
                            Gson gson = new Gson();
                            List<Task> ts = gson.fromJson(response.body(), new TypeToken<List<Task>>() {
                            }.getType());
                            for (Task task : ts) {
                                dao.insert(task);
                            }
                            refresh(EqCheckListActivity.class);
                            DialogUIUtils.showToast("下载成功");
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        DialogUIUtils.showToast("下载失败");
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

    /**
     * 清空任务（本地数据库）
     */
    @OnClick(R.id.btn_task_clear)
    public void clearTask() {
        DialogUIUtils
                .showIsDeleteSheetDialog(mContext, "删除数据，谨慎操作", new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                dao.deleteAll();
                refresh(EqCheckListActivity.class);
                showToast("数据已全部删除");
            }
        });
    }
}
