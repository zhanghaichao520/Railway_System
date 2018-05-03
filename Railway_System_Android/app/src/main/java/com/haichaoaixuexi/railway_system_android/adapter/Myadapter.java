package com.haichaoaixuexi.railway_system_android.adapter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haichaoaixuexi.railway_system_android.R;

import java.util.List;

/**
 * Created by haichaoaixuexi on 2018/2/19.
 */

public class Myadapter extends BaseQuickAdapter<String,BaseViewHolder> {


    public Myadapter(@LayoutRes int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_item_title,item);
        helper.addOnClickListener(R.id.tv_item_click);
        helper.addOnClickListener(R.id.tv_item_title);
    }
}
