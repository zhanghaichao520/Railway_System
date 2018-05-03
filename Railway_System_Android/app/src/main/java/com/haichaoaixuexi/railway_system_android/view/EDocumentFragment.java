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
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haichaoaixuexi.railway_system_android.R;
import com.haichaoaixuexi.railway_system_android.adapter.Myadapter;
import com.haichaoaixuexi.railway_system_android.app.DocumentActivity;
import com.haichaoaixuexi.railway_system_android.data.Const;
import com.haichaoaixuexi.railway_system_android.entity.EDocument;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zwy.kutils.widget.loadingdialog.DialogUIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EDocumentFragment extends Fragment {
    @BindView(R.id.list_edocument)
    RecyclerView listEdocument;
    Unbinder unbinder;
    //列表显示相关
    private Myadapter mAdapter = null;
    private List<String> lists = new ArrayList<>();
    private List<EDocument> documents = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_edocument, container, false);

        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lists.clear();
        lists.add("废止部分规范性文件");
        downloadFromSever();

    }

    private void downloadFromSever() {
        final Dialog dialog = DialogUIUtils.showLoadingHorizontal(getActivity(), "数据加载中").show();
        OkGo.<String>post(Const.URL_GET_DOCUMENTS)
                .tag(this)
                .params("action", "getAll")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.body().equals("failed")) {
                            Toast.makeText(getActivity(), "服务器异常",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Gson gson = new Gson();
                            documents = gson.fromJson(response.body(), new TypeToken<List<EDocument>>() {
                            }.getType());
                            for (EDocument document : documents) {
                                lists.add(document.getTITLE());
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
        listEdocument.setLayoutManager(layoutManager);
        listEdocument.setAdapter(mAdapter);
        //适配器Item点击事件
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_item_click:
                        //showToast("您点击了第" + (position + 1) + "条数据的子view");
                        Intent intent = new Intent(getActivity(), DocumentActivity.class);
                        if (position != 0)
                            intent.putExtra("document", documents.get(position - 1));
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
        }, listEdocument);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
