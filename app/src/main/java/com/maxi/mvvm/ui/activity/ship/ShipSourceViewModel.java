package com.maxi.mvvm.ui.activity.ship;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;

import com.maxi.mvvm.bean.FindShipBean;
import com.maxi.mvvm.base.ToolbarViewModel;
import com.maxi.mvvm.http.BaseObserver;
import com.maxi.mvvm.http.DataManager;
import com.maxi.mvvm.http.ExceptionTypeAHandler;
import com.maxi.mvvm.http.HttpLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by maxi on 2021/10/12.
 */
public class ShipSourceViewModel extends ToolbarViewModel {

    private HttpLiveData<FindShipBean> datas = new HttpLiveData<FindShipBean>();

    public ShipSourceViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public HttpLiveData<FindShipBean> getDatas() {
        return datas;
    }

    public void initToolbar() {
        setBackTextVisible(View.VISIBLE);
        setTitleText("船源信息详情");
    }

    public void getShipDetail(String shipId) {
        DataManager.getFindShip("", "", shipId, 99999, 1)
                .map(new ExceptionTypeAHandler<List<FindShipBean>>())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(this)//请求与ViewModel周期同步
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<FindShipBean>>(datas) {

                    @Override
                    public void onSuccess(@NotNull List<FindShipBean> findShipBeans) {
                        datas.setSuccess(findShipBeans.get(0));
                    }

                    @Override
                    public void showError(String msg) {
                        datas.setError(msg);
                        toastMsg.setValue(msg);
                    }
                });
    }
}