package com.maxi.mvvm.ui.activity.ship;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.Observer;

import com.chad.library.BR;
import com.maxi.mvvm.R;
import com.maxi.mvvm.bean.FindShipBean;
import com.maxi.mvvm.databinding.ActivityShipSourceBinding;
import com.maxi.mvvm.utils.TimeTools;
import com.maxi.mvvm.base.BaseActivity;
import com.maxi.mvvm.http.HttpLiveData;
import com.maxi.mvvm.http.HttpObserve;
import com.maxi.mvvm.ui.activity.main.MainViewModel;

import java.io.Serializable;

/**
 * 船源信息详情页
 * Created by maxi on 2021/10/12.
 */
public class ShipSourceActivity extends BaseActivity<ActivityShipSourceBinding, ShipSourceViewModel> {

    private final static String EXTRA_KEY_DATAS = "extra_key_datas";

    public static void open(Context context, FindShipBean data) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_KEY_DATAS, (Serializable) data);
        Intent intent = new Intent(context, ShipSourceActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_ship_source;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void init() {
        viewModel.initToolbar();
        Intent intent = getIntent();
        FindShipBean datas = null;
        if (intent != null) {
            binding.setDataBean(datas = (FindShipBean) intent.getSerializableExtra(EXTRA_KEY_DATAS));
            setInitData(datas);
        }
        viewModel.getDatas().observe(this, new HttpObserve<FindShipBean>() {
            @Override
            public void onSuccess(FindShipBean findShipBean) {
                binding.setDataBean(findShipBean);
                setInitData(findShipBean);
            }
        });
        if (datas != null) {
            viewModel.getShipDetail(datas.getId() + "");
        }
    }

    private void setInitData(FindShipBean datasBean) {
        if (datasBean == null) {
            return;
        }
//        String userId = MyApplication.userInfoDataBean == null || String.valueOf(MyApplication.userInfoDataBean.getDatas().getUserId()) == null ? "" : String.valueOf(MyApplication.userInfoDataBean.getDatas().getUserId());
//        if (userId.equals(datasBean.getUserId() + "")) {
//            groupForMineShip.setVisibility(View.VISIBLE);
//            groupNotMineShip.setVisibility(View.GONE);
//        } else {
//            groupNotMineShip.setVisibility(View.VISIBLE);
//            groupForMineShip.setVisibility(View.GONE);
//        }
        ((TextView) findViewById(R.id.tv_user_name)).setText(datasBean.getUsername());
        if (datasBean.getIsVip() == 1) {
            findViewById(R.id.iv_vip_icon).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.iv_vip_icon).setVisibility(View.GONE);
        }
        binding.clShipName.setTextValue(datasBean.getTransportNumber());
        binding.clShipTon.setTextValue(datasBean.getLoadingTons() + "吨");
        binding.clShipPlace.setTextValue(datasBean.getShipPortName());
        binding.clShipAddress.setTextValue(datasBean.getLoadingAddress());
        binding.clShipDate.setTextValue(TimeTools.formateTrackTime(datasBean.getFreeDate()) + "±" + datasBean.getFloatingDays() + "天");
        binding.clShipTag.setTextValue(datasBean.getRemark());
    }

}