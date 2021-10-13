package com.maxi.mvvm.ui.fragment.ship;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gyf.immersionbar.ImmersionBar;
import com.maxi.mvvm.BR;
import com.maxi.mvvm.R;
import com.maxi.mvvm.base.BaseFragment;
import com.maxi.mvvm.databinding.FragmentMyshipBinding;
import com.maxi.mvvm.ui.adapter.ShipRVAdapter;

/**
 * Created by maxi on 2021/9/13.
 */
public class ShipFragment extends BaseFragment<FragmentMyshipBinding, ShipViewModel> {
    public static ShipFragment getInstance() {
        return new ShipFragment();
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return R.layout.fragment_myship;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {
        viewModel.initToolbar();

        binding.rvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        viewModel.setAdapter(new ShipRVAdapter());
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