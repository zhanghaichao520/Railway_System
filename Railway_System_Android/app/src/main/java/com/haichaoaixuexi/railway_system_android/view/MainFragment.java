package com.haichaoaixuexi.railway_system_android.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haichaoaixuexi.railway_system_android.R;
import com.haichaoaixuexi.railway_system_android.app.ChangePasswordActivity;
import com.haichaoaixuexi.railway_system_android.app.DataSyncActivity;
import com.haichaoaixuexi.railway_system_android.app.EqCheckHistoryActivity;
import com.haichaoaixuexi.railway_system_android.app.EqCheckListActivity;
import com.haichaoaixuexi.railway_system_android.app.EqRepairListActivity;
import com.haichaoaixuexi.railway_system_android.app.EquimentInfoActivity;
import com.haichaoaixuexi.railway_system_android.app.FeedBackActivity;
import com.haichaoaixuexi.railway_system_android.app.LoginActivity;
import com.haichaoaixuexi.railway_system_android.data.Const;
import com.haichaoaixuexi.railway_system_android.utils.GeneralUtil;
import com.makeramen.roundedimageview.RoundedImageView;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;
import com.zhy.view.CircleMenuLayout;
import com.zwy.kutils.widget.loadingdialog.DialogUIUtils;
import com.zwy.kutils.widget.loadingdialog.alertdialog.ActionSheetDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static com.haichaoaixuexi.railway_system_android.data.Const.REQUEST_CODE_SCAN;

/**
 * Created by haichaoaixuexi on 2018/2/12.
 */

public class MainFragment extends Fragment {
    @BindView(R.id.img_portrait)
    RoundedImageView imgPortrait;
    @BindView(R.id.txt_info)
    TextView txtInfo;
    @BindView(R.id.img_go)
    ImageView imgGo;
    @BindView(R.id.layout_user)
    RelativeLayout layoutUser;
    @BindView(R.id.id_circle_menu_item_center)
    RelativeLayout idCircleMenuItemCenter;
    @BindView(R.id.id_menulayout)
    CircleMenuLayout idMenulayout;
    Unbinder unbinder;
    private CircleMenuLayout mCircleMenuLayout;
    private String[] mItemTexts = new String[]{"设备巡检 ", "设备点检", "检修验收",
            "问题反馈", "数据同步", "修改密码"};
    private int[] mItemImgs = new int[]{R.drawable.btn_xunjian,
            R.drawable.btn_dianjian, R.drawable.btn_guzhang,
            R.drawable.btn_zijin, R.drawable.btn_data,
            R.drawable.btn_xiugaimima};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        TextView txt_name = view.findViewById(R.id.txt_name);

        if (Const.currentuser!=null){
            txt_name.setText(Const.currentuser.getUSER_NAME());
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCircleMenuLayout = (CircleMenuLayout) getView().findViewById(R.id.id_menulayout);
        mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);
        mCircleMenuLayout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener() {
            @Override
            public void itemClick(View view, int pos) {
                if (Const.currentuser!=null){
                    switch (pos){
                        case 0:
                            xunjian();
                            break;
                        case 1:
                            startActivity(new Intent(getActivity(), EqCheckListActivity.class));
                            break;
                        case 2:
                            startActivity(new Intent(getActivity(), EqRepairListActivity.class));
                            break;
                        case 3:
                            startActivity(new Intent(getActivity(), FeedBackActivity.class));
                            break;
                        case 4:
                            startActivity(new Intent(getActivity(), DataSyncActivity.class));
                            break;
                        case 5:
                            startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
                        default:break;
                    }
                }else {
                    DialogUIUtils.showOnlyOneButtonAlertDialog(getActivity(), "请先登陆后再操作", "去登陆", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            GeneralUtil.startActivity(getActivity(), LoginActivity.class);
                        }
                    });
                }

            }
            @Override
            public void itemCenterClick(View view) {
                if (Const.currentuser == null) {
                    DialogUIUtils.showToastCenter("请先登陆");
                    return;
                }else if (Const.currentuser.getROLE_ID()>2) {
                    DialogUIUtils.showToastCenter("权限不足");
                    return;
                }
                final EditText inputServer = new EditText(getContext());
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("设备检修历史").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer).setNegativeButton("取消",
                        null);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String SBBH = inputServer.getText().toString().trim();
                        if (SBBH.length()==9) {
                            Intent intent = new Intent(getActivity(), EqCheckHistoryActivity.class);
                            intent.putExtra("SBBH", SBBH);
                            startActivity(intent);
                        } else {
                            GeneralUtil.toast(getContext(), "请输入合法的设备号");
                        }
                    }
                });
                builder.show();
            }
        });
    }

    private void xunjian() {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                //二维码内容
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                char type = content.charAt(0);
                content = content.substring(1);
                if (type==Const.TYPE_EQUIMENT){
                   // DialogUIUtils.showToast(content);
                    //界面跳转
                    Intent intent = new Intent(getActivity(),EquimentInfoActivity.class);
                    intent.putExtra("SBBH",content);
                    intent.putExtra("type", "check");
                    startActivity(intent);
                }else {
                    DialogUIUtils.showToast("二维码类别不正确");
                }
            }
        }
    }

    @OnClick(R.id.layout_user)
    public void goToLogin(){
        if (Const.currentuser==null){
            GeneralUtil.startActivity(getActivity(), LoginActivity.class);
        }else {
            new ActionSheetDialog(getContext())
                    .builder()
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .setTitle("提示")
                    .addSheetItem("版本更新", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {

                        }
                    })
                    .addSheetItem("注销登陆", ActionSheetDialog.SheetItemColor.Red, new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {
                            Const.currentuser = null;
                            GeneralUtil.startActivity(getActivity(), LoginActivity.class);
                            DialogUIUtils.showToast("注销成功");
                        }
                    }).show();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
