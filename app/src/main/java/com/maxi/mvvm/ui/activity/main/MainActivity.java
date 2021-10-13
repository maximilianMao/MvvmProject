package com.maxi.mvvm.ui.activity.main;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.gyf.immersionbar.ImmersionBar;
import com.maxi.mvvm.BR;
import com.maxi.mvvm.R;
import com.maxi.mvvm.base.BaseActivity;
import com.maxi.mvvm.databinding.ActivityMainBinding;
import com.maxi.mvvm.ui.adapter.MainVPAdapter;
import com.maxi.mvvm.ui.fragment.ship.ShipFragment;
import com.maxi.mvvm.ui.fragment.home.HomeFragment;
import com.maxi.mvvm.ui.fragment.goods.GoodsFragment;
import com.maxi.mvvm.ui.fragment.mine.MineFragment;
import com.maxi.mvvm.ui.fragment.price.PriceFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {

    private List<String> titles = new ArrayList<String>() {
        {
            add("找船");
            add("找货");
            add("定位");
            add("运价");
            add("我的");
        }
    };
    private List<Integer> icons = new ArrayList<Integer>() {
        {
            add(R.drawable.selector_main_tab_ship);
            add(R.drawable.selector_main_tab_goods);
            add(R.drawable.selector_main_tab_location);
            add(R.drawable.selector_main_tab_price);
            add(R.drawable.selector_main_tab_mine);
        }
    };
    private List<Fragment> fragments = new ArrayList<Fragment>() {
        {
            add(ShipFragment.getInstance());
            add(GoodsFragment.getInstance());
            add(HomeFragment.getInstance());
            add(PriceFragment.getInstance());
            add(MineFragment.getInstance());
        }
    };

    private MainVPAdapter mainVPAdapter;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void init() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .init();
        mainVPAdapter = new MainVPAdapter(getSupportFragmentManager(), getLifecycle(), fragments);
        binding.viewPager.setAdapter(mainVPAdapter);
        binding.viewPager.setUserInputEnabled(false);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, true, false, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles.get(position));
                tab.setIcon(ContextCompat.getDrawable(MainActivity.this, icons.get(position)));
            }
        }).attach();
    }

    @Override
    protected boolean needInitImmersionBar() {
        return false;
    }

    @Override
    protected boolean noToolBar() {
        return true;
    }

}