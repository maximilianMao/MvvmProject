package com.maxi.mvvm.ui.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.gyf.immersionbar.ImmersionBar;
import com.maxi.mvvm.BR;
import com.maxi.mvvm.R;
import com.maxi.mvvm.base.BaseFragment;
import com.maxi.mvvm.databinding.FragmentHomeBinding;

/**
 * Created by maxi on 2021/9/13.
 */
public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> {

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return R.layout.fragment_home;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {
        viewModel.initToolbar();
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