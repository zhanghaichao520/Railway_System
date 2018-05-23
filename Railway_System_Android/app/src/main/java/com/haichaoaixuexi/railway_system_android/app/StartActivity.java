package com.haichaoaixuexi.railway_system_android.app;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;

import com.haichaoaixuexi.railway_system_android.R;
import com.haichaoaixuexi.railway_system_android.view.EDocumentFragment;
import com.haichaoaixuexi.railway_system_android.view.LogFragment;
import com.haichaoaixuexi.railway_system_android.view.MainFragment;
import com.haichaoaixuexi.railway_system_android.view.PersonalFragment;
import com.lzy.widget.AlphaIndicator;
import com.zwy.kutils.utils.AppManager;
import com.zwy.kutils.widget.loadingdialog.DialogUIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haichaoaixuexi on 2018/2/12.
 * describe:app主界面
 */

public class StartActivity extends BaseActivity {
    MainAdapter adapter;
    MainFragment mainFragment = new MainFragment();
    PersonalFragment personalFragment = new PersonalFragment();
    EDocumentFragment eDocumentFragment = new EDocumentFragment();
    LogFragment logFragment = new LogFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.view_bottom);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new MainAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        AlphaIndicator alphaIndicator = (AlphaIndicator) findViewById(R.id.alphaIndicator);
        alphaIndicator.setViewPager(viewPager);
    }

    @Override
    protected void initData() {

    }


    private class MainAdapter extends FragmentPagerAdapter {
        private List<android.support.v4.app.Fragment> fragments = new ArrayList<>();
        private String[] titles = {//
                "第一页\n\n重点看下面的的图标是渐变色，随着滑动距离的增加，颜色逐渐过度",//
                "第二页\n\n重点看下面的的图标是渐变色，随着滑动距离的增加，颜色逐渐过度",//
                "第三页\n\n重点看下面的的图标是渐变色，随着滑动距离的增加，颜色逐渐过度", //
                "第四页\n\n重点看下面的的图标是渐变色，随着滑动距离的增加，颜色逐渐过度"};

        public MainAdapter(FragmentManager fm) {
            super(fm);

            fragments.add(mainFragment);
            fragments.add(eDocumentFragment);
            fragments.add(logFragment);
            fragments.add(personalFragment);

        }
        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return fragments.get(position);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 是否退出应用
            DialogUIUtils.showTwoButtonAlertDialog(this, "提示", "是否退出应用", "取消", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            }, "退出", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppManager.getAppManager().AppExit(StartActivity.this,false);
                }
            },false);
        }
        return super.onKeyDown(keyCode, event);
    }
}
