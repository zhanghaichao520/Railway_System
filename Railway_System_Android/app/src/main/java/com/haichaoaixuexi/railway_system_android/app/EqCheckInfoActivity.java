package com.haichaoaixuexi.railway_system_android.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haichaoaixuexi.railway_system_android.R;
import com.haichaoaixuexi.railway_system_android.data.Const;
import com.haichaoaixuexi.railway_system_android.entity.Eq_check;
import com.haichaoaixuexi.railway_system_android.entity.Eq_issue_kind;
import com.haichaoaixuexi.railway_system_android.greendao.Eq_checkDao;
import com.haichaoaixuexi.railway_system_android.greendao.Eq_issue_kindDao;
import com.haichaoaixuexi.railway_system_android.utils.PhotoUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.soundcloud.android.crop.Crop;
import com.zwy.kutils.utils.Log;
import com.zwy.kutils.widget.loadingdialog.DialogUIUtils;
import com.zwy.kutils.widget.loadingdialog.alertdialog.ActionSheetDialog;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by haichao on 2018/2/22.
 * 至尊宝：长夜漫漫无心睡眠，我以为只有我睡不着，原来晶晶姑娘你也睡不着 ！
 * describe: 设备检修信息填写
 */
public class EqCheckInfoActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.spi_JCLX)
    Spinner spiJCLX;
    @BindView(R.id.setSBBH)
    TextView setSBBH;
    @BindView(R.id.spi_GZLX)
    Spinner spiGZLX;
    @BindView(R.id.clip_pic_title)
    TextView clipPicTitle;
    @BindView(R.id.clip_pic)
    ImageView clipPic;
    @BindView(R.id.btn_save)
    TextView btnSave;
    @BindView(R.id.btn_upload)
    TextView btnUpload;
    @BindView(R.id.spi_GZDJ)
    Spinner spiGZDJ;
    @BindView(R.id.edt_GZMS)
    EditText edtGZMS;
    @BindView(R.id.txt_JCLX)
    TextView txtJCLX;
    @BindView(R.id.txt_GZLX)
    TextView txtGZLX;
    @BindView(R.id.txt_GZDJ)
    TextView txtGZDJ;
    @BindView(R.id.btn_sync)
    TextView btnSync;
    private String path;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    private List<String> gzlist = new ArrayList<>();
    private ArrayAdapter<String> spiAdapter;
    private Appliaction myApp;
    private Eq_issue_kindDao dao;
    private Eq_issue_kind issue;
    private Eq_checkDao eq_checkDao;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_eq_check_info);
        ButterKnife.bind(this);
        titleTxt.setText("填写设备检修信息");
    }

    @Override
    protected void initData() {
        myApp = (Appliaction) getApplication();
        dao = myApp.getDaoSession().getEq_issue_kindDao();
        eq_checkDao = myApp.getDaoSession().getEq_checkDao();
        final Intent intent = getIntent();
        String checkString = intent.getStringExtra("check");
        if (checkString != null && (!checkString.equals(""))) {
            //控件可见度调节
            btnSave.setVisibility(View.GONE);
            btnUpload.setVisibility(View.GONE);
            spiGZLX.setVisibility(View.GONE);
            spiGZDJ.setVisibility(View.GONE);
            spiJCLX.setVisibility(View.GONE);
            clipPicTitle.setVisibility(View.GONE);
            txtGZDJ.setVisibility(View.VISIBLE);
            txtGZLX.setVisibility(View.VISIBLE);
            txtJCLX.setVisibility(View.VISIBLE);
            clipPic.setVisibility(View.VISIBLE);
            btnSync.setVisibility(View.VISIBLE);
            Eq_check check = gson.fromJson(checkString, Eq_check.class);
            txtJCLX.setText(check.getJCLX());
            setSBBH.setText(check.getSBBH() + "");
            issue = dao.queryBuilder().where(Eq_issue_kindDao.Properties.EQ_ISSUE_KIND.eq(check.getGZLX())).unique();
            if (issue != null)
                txtGZLX.setText(issue.getEQ_ISSUE());
            txtGZDJ.setText(check.getABC());
            //图片
            File file = new File(check.getGZTP());
            if (file.exists()) {
                Bitmap bm = BitmapFactory.decodeFile(check.getGZTP());
                clipPic.setImageBitmap(bm);
            }else {
                btnSync.setVisibility(View.GONE);
            }
            edtGZMS.setText(check.getGZMS());
        } else {
            String SBBH = intent.getStringExtra("SBBH");
            setSBBH.setText(SBBH);
            String GZLX = intent.getStringExtra("GZLX");
            String[] gz = GZLX.split(",");
            int count = 0;
            for (int i = 0; i < gz.length; i++) {
                issue = dao.queryBuilder().where(Eq_issue_kindDao.Properties.EQ_ISSUE_KIND.eq(Integer.parseInt(gz[i]))).unique();
                if (issue != null)
                    gzlist.add(issue.getEQ_ISSUE());
                else {
                    count++;
                    continue;
                }
            }
            if (count == gz.length) {
                gzlist.add("类型不明(请同步故障信息)");
            }
            spiAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, gzlist);
            spiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spiGZLX.setAdapter(spiAdapter);
        }
    }

    @OnClick(R.id.clip_pic_title)
    public void options() {
        //申请读写sd卡的权限
        verifyStoragePermissions(this);
        ActionSheetDialog mDialog = new ActionSheetDialog(this).builder();
        mDialog.setTitle("选择");
        mDialog.setCancelable(false);
        mDialog.addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                PhotoUtil.photograph(mContext);
            }
        }).addSheetItem("从相册选取", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                //PhotoUtil.selectPictureFromAlbum(EqCheckInfoActivity.this);
                Crop.pickImage(mContext);
            }
        }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == PhotoUtil.NONE)
            return;
        // 拍照
        if (requestCode == PhotoUtil.PHOTOGRAPH) {
            // 设置文件保存路径这里放在跟目录下
            File picture = null;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                picture = new File(Environment.getExternalStorageDirectory() + PhotoUtil.imageName);
                if (!picture.exists()) {
                    picture = new File(Environment.getExternalStorageDirectory() + PhotoUtil.imageName);
                }
            } else {
                picture = new File(this.getFilesDir() + PhotoUtil.imageName);
                if (!picture.exists()) {
                    picture = new File(mContext.getFilesDir() + PhotoUtil.imageName);
                }
            }
            path = PhotoUtil.getPath(this);// 生成一个地址用于存放剪辑后的图片
            if (TextUtils.isEmpty(path)) {
                Log.e("随机生成的用于存放剪辑后的图片的地址失败");
                return;
            }
            Uri imageUri = Uri.fromFile(new File(path));
            //PhotoUtil.startPhotoZoom(mContext, Uri.fromFile(picture), PhotoUtil.PICTURE_HEIGHT, PhotoUtil.PICTURE_WIDTH,imageUri);
            Crop.of(Uri.fromFile(picture), imageUri).asSquare().start(this);
        }
        if (data == null) {
            showToast("data null");
            return;
        }
        // 读取相册缩放图片
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            path = PhotoUtil.getPath(this);// 生成一个地址用于存放剪辑后的图片
            if (TextUtils.isEmpty(path)) {
                Log.e("随机生成的用于存放剪辑后的图片的地址失败");
                return;
            }
            Uri imageUri = Uri.fromFile(new File(path));
            Crop.of(data.getData(), imageUri).asSquare().start(this);
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
            showToast(path);
        }
    }

    @OnClick(R.id.btn_back)
    public void back() {
        mContext.finish();
    }


    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            clipPic.setImageURI(Crop.getOutput(result));
            clipPic.setVisibility(View.VISIBLE);
        } else if (resultCode == Crop.RESULT_ERROR) {
            showToast(Crop.getError(result).getMessage());
        }
    }

    /**
     * 权限申请
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_save)
    public void save() {
        Eq_check ec = new Gson().fromJson(getEcBean(), Eq_check.class);
        eq_checkDao.insert(ec);
        DialogUIUtils.showOnlyOneButtonAlertDialog(mContext, "数据保存成功", "确认", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGoThenKill(StartActivity.class);
            }
        });
    }

    @OnClick(R.id.btn_upload)
    public void upload() {
        final PostRequest<String> request = OkGo.<String>post(Const.URL_EQ_CHECK)
                .tag(this)
                .params("bean", getEcBean());
        if (path != null && path != "") {
            request.params("GZTP", new File(path))
                    .isMultipart(true);
        } else {
            showToast("未选择图片");
            return;
        }
        final Dialog dialog = DialogUIUtils.showLoadingHorizontal(mContext, "数据上传中").show();
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
    @OnClick(R.id.btn_sync)
    public void sync() {
        String checkString = getIntent().getStringExtra("check");
        final Eq_check check = gson.fromJson(checkString, Eq_check.class);
        final PostRequest<String> request = OkGo.<String>post(Const.URL_EQ_CHECK)
                .tag(this)
                .params("bean", checkString);
        if (check.getGZTP() != null && check.getGZTP() != "") {
            request.params("GZTP", new File(check.getGZTP()))
                    .isMultipart(true);
        } else {
            showToast("未选择图片");
            return;
        }
        final Dialog dialog = DialogUIUtils.showLoadingHorizontal(mContext, "数据上传中").show();
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

    private String getEcBean() {
        Eq_check ec = new Eq_check();
        ec.setJCLX(spiJCLX.getSelectedItem().toString());
        ec.setSBBH(Integer.parseInt(setSBBH.getText().toString().trim()));
        issue = dao.queryBuilder().where(Eq_issue_kindDao.Properties.EQ_ISSUE.eq(spiGZLX.getSelectedItem().toString())).unique();
        if (issue != null)
            ec.setGZLX(issue.getEQ_ISSUE_KIND());
        else {
            ec.setGZLX(0);
        }
        ec.setGZMS(edtGZMS.getText().toString().trim());
        ec.setBXR(Const.currentuser.getUSER_ID());
        ec.setBXSJ(new Timestamp(System.currentTimeMillis()).toString());
        ec.setABC(spiGZDJ.getSelectedItem().toString().charAt(0) + "");
        ec.setGZTP(path);
        ec.setYSR(0);
        Gson gson = new Gson();
        return gson.toJson(ec);
    }
}
