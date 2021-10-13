package com.maxi.mvvm.ui.fragment.price;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.gyf.immersionbar.ImmersionBar;
import com.maxi.mvvm.BR;
import com.maxi.mvvm.R;
import com.maxi.mvvm.base.BaseFragment;
import com.maxi.mvvm.databinding.FragmentYunjiaBinding;

/**
 * Created by maxi on 2021/9/13.
 */
public class PriceFragment extends BaseFragment<FragmentYunjiaBinding, PriceViewModel> {

    public static PriceFragment getInstance() {
        return new PriceFragment();
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return R.layout.fragment_yunjia;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {

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