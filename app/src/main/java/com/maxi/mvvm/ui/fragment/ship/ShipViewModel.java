package com.maxi.mvvm.ui.fragment.ship;

import android.app.Application;

import androidx.annotation.NonNull;

import com.maxi.mvvm.base.BaseRefreshLoadViewModel;
import com.maxi.mvvm.bean.FindShipBean;
import com.maxi.mvvm.http.DataManager;
import com.maxi.mvvm.http.ExceptionTypeAHandler;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by maxi on 2021/9/13.
 */
public class ShipViewModel extends BaseRefreshLoadViewModel<FindShipBean> {

    private String shipPort = "";
    private String freeTime = "";

    public ShipViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    @Override
    protected Observable<List<FindShipBean>> setHttpApi(boolean pullToRefresh) {
        return DataManager.getFindShip(shipPort, freeTime, "", PAGE_SIZE, handlePaging(pullToRefresh))
                .map(new ExceptionTypeAHandler<List<FindShipBean>>());
    }

    public void initToolbar() {
        setTitleText("船盘列表");
    }

    public void setShipPort(String shipPort) {
        this.shipPort = shipPort;
    }

    public void setFreeTime(String freeTime) {
        this.freeTime = freeTime;
    }

}