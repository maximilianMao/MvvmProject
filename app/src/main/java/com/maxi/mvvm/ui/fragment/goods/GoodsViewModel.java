package com.maxi.mvvm.ui.fragment.goods;

import android.app.Application;

import androidx.annotation.NonNull;

import com.maxi.mvvm.base.BaseRefreshLoadViewModel;
import com.maxi.mvvm.bean.HuoPanListBean;
import com.maxi.mvvm.http.DataManager;
import com.maxi.mvvm.http.ExceptionTypeBHandler;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by maxi on 2021/9/13.
 */
public class GoodsViewModel extends BaseRefreshLoadViewModel<HuoPanListBean> {
    public GoodsViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    @Override
    protected Observable<List<HuoPanListBean>> setHttpApi(boolean pullToRefresh) {
        return DataManager.getHuopanList(handlePaging(pullToRefresh), PAGE_SIZE, 2, "1", "", "", "", "", "", "")
                .map(new ExceptionTypeBHandler<List<HuoPanListBean>>());
    }

    public void initToolbar() {
        setTitleText("货盘列表");
    }
}