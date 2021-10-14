package com.maxi.mvvm.ui.fragment.goods;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gyf.immersionbar.ImmersionBar;
import com.maxi.mvvm.BR;
import com.maxi.mvvm.R;
import com.maxi.mvvm.base.BaseFragment;
import com.maxi.mvvm.bean.HuoPanListBean;
import com.maxi.mvvm.common.aspectjx.AllowDoubleClick;
import com.maxi.mvvm.databinding.FragmentHuopanBinding;
import com.maxi.mvvm.ui.adapter.GoodsRVAdapter;

/**
 * Created by maxi on 2021/9/13.
 */
public class GoodsFragment extends BaseFragment<FragmentHuopanBinding, GoodsViewModel> {
    public static GoodsFragment getInstance() {
        return new GoodsFragment();
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return R.layout.fragment_huopan;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {
        viewModel.initToolbar();

        binding.rvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        viewModel.setAdapter(new GoodsRVAdapter());
        binding.rvContent.setAdapter(viewModel.getAdapter());
        viewModel.setRefreshLayout(binding.srlContent);

        viewModel.getAllData(true);
    }

    @Override
    public void initImmersionBar() {
        super.initImmersionBar();
        try {
            ImmersionBar.with(this)
                    .titleBar(binding.ltvTop.rlToolbar)
                    .init();
        } catch (Exception e) {//防止未在宿主activity里初始化的异常
            e.printStackTrace();
        }
    }
}