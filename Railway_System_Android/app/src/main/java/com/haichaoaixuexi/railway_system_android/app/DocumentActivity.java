package com.haichaoaixuexi.railway_system_android.app;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.haichaoaixuexi.railway_system_android.R;
import com.haichaoaixuexi.railway_system_android.entity.EDocument;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by haichao on 2018/3/17.
 * 至尊宝：长夜漫漫无心睡眠，我以为只有我睡不着，原来晶晶姑娘你也睡不着 ！
 * describe: 电子公文详细信息展示
 */

public class DocumentActivity extends BaseActivity {
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.tit)
    TextView tit;
    @BindView(R.id.con)
    TextView con;
    @BindView(R.id.sj)
    TextView sj;
    @BindView(R.id.source)
    TextView source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_document);
        ButterKnife.bind(this);
        titleTxt.setText("电子公文");
    }

    @Override
    protected void initData() {
        EDocument document = (EDocument) getIntent().getSerializableExtra("document");
        if (document != null) {
            tit.setText(document.getTITLE());
            con.setText("\t\t\t"+document.getCONTENT());
            sj.setText("发布时间："+document.getCJRQ());
            source.setText("来源："+document.getUSER_NAME());
        }
    }

    @OnClick(R.id.btn_back)
    public void back() {
        mContext.finish();
    }
}
