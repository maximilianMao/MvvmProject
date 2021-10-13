package com.maxi.mvvm.ui.activity.ship;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.maxi.mvvm.base.ToolbarViewModel;
import com.maxi.mvvm.bean.FindShipBean;
import com.maxi.mvvm.common.LoadState;
import com.maxi.mvvm.http.BaseConsumer;
import com.maxi.mvvm.http.BaseOnErrorConsumer;
import com.maxi.mvvm.http.DataManager;
import com.maxi.mvvm.http.ExceptionTypeAHandler;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by maxi on 2021/10/12.
 */
public class ShipSourceViewModel extends ToolbarViewModel {

    private MutableLiveData<FindShipBean> datas = new MutableLiveData<FindShipBean>();

    public ShipSourceViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public MutableLiveData<FindShipBean> getDatas() {
        return datas;
    }

    public void initToolbar() {
        setBackTextVisible(View.VISIBLE);
        setTitleText("船源信息详情");
    }

    public void getShipDetail(String shipId) {
        addSubscribe(DataManager.getFindShip("", "", shipId, 99999, 1)
                .map(new ExceptionTypeAHandler<List<FindShipBean>>())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(this)//请求与ViewModel周期同步
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseConsumer<List<FindShipBean>>() {

                    @Override
                    public void success(List<FindShipBean> baseResp) {
                        loadState.setValue(LoadState.SUCCESS);
                        datas.setValue(baseResp.get(0));
                    }
                }, new BaseOnErrorConsumer() {
                    @Override
                    public void showError(String msg) {
                        toastMsg.setValue(msg);
                    }
                }));
    }
}