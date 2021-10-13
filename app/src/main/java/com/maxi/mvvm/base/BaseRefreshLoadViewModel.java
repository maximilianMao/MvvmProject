package com.maxi.mvvm.base;

import android.app.Application;
import android.view.LayoutInflater;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.maxi.mvvm.R;
import com.maxi.mvvm.common.LoadState;
import com.maxi.mvvm.http.BaseConsumer;
import com.maxi.mvvm.http.BaseOnErrorConsumer;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by maxi on 2021/10/11.
 */
public abstract class BaseRefreshLoadViewModel<T> extends ToolbarViewModel {

    private BaseQuickAdapter adapter;
    private SmartRefreshLayout refreshLayout;
    private int pageFrom = 1;
    protected final int PAGE_SIZE = 10;
    protected Disposable disposable;//用于管理请求 防止弱网情况 频繁刷新 注：只针对setHttpApi请求，如有其他请求需要管理则需要另外创建对象
    public MutableLiveData<Integer> loadStatus = new MutableLiveData<Integer>();

    @IntDef({LoadMoreNoDataStatus.REFRESH_RESET_NO_MORE_DATA,
            LoadMoreNoDataStatus.LOAD_MORE_WITH_NO_MORE_DATA,
            LoadMoreNoDataStatus.DROP_DOWN_REFRESH,
            LoadMoreNoDataStatus.PULL_UP_LOADING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LoadMoreNoDataStatus {
        int REFRESH_RESET_NO_MORE_DATA = 0;//刷新时重置 恢复adapter没有更多数据的原始状态
        int LOAD_MORE_WITH_NO_MORE_DATA = 1;//上拉加载没有数据显示没有更多数据状态
        int DROP_DOWN_REFRESH = 2;
        int PULL_UP_LOADING = 3;
    }

    public MutableLiveData<Integer> getLoadStatus() {
        return loadStatus;
    }

    public BaseRefreshLoadViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    /**
     * 设置下拉刷新 上拉加载所需接口
     *
     * @param pullToRefresh
     * @return
     */
    protected abstract Observable<List<T>> setHttpApi(boolean pullToRefresh);

    public void getAllData(boolean pullToRefresh) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();//如果上次请求仍在继续 则直接终止 另起请求 防止频繁调用接口
        }
        if (pullToRefresh) {
            //如果下拉刷新 则重置没有更多数据状态
            if (refreshLayout != null) {
                refreshLayout.resetNoMoreData();
            }
            loadStatus.setValue(LoadMoreNoDataStatus.REFRESH_RESET_NO_MORE_DATA);
        }
        addSubscribe(
                disposable = setHttpApi(pullToRefresh)
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(this)//请求与ViewModel周期同步
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseConsumer<List<T>>() {

                            @Override
                            public void success(List<T> baseResp) {
                                loadState.postValue(LoadState.SUCCESS);
                                /*
                                 *刷新 清空数据
                                 */
                                if (pullToRefresh) {
                                    /*
                                     *  下拉刷新
                                     */
                                    getAdapter().setList(baseResp);
                                    if (refreshLayout != null) {
                                        refreshLayout.finishRefresh();
                                    }
                                    loadStatus.setValue(LoadMoreNoDataStatus.DROP_DOWN_REFRESH);
                                } else {
                                    /*
                                     *  加载数据
                                     */
                                    getAdapter().addData(baseResp);
                                    if (refreshLayout != null) {
                                        refreshLayout.finishLoadMore();
                                    }
                                    loadStatus.setValue(LoadMoreNoDataStatus.PULL_UP_LOADING);
                                }
                            }
                        }, new BaseOnErrorConsumer() {
                            @Override
                            public void showError(String msg) {
                                if (pullToRefresh) {
                                    if (refreshLayout != null) {
                                        refreshLayout.finishRefresh();
                                    }
                                    loadStatus.setValue(LoadMoreNoDataStatus.DROP_DOWN_REFRESH);
                                    loadState.postValue(LoadState.ERROR);
                                    errorMsg.set(msg);
                                } else {
                                    if (refreshLayout != null) {
                                        refreshLayout.finishLoadMore();
                                    }
                                    loadStatus.setValue(LoadMoreNoDataStatus.PULL_UP_LOADING);
                                    toastMsg.setValue(msg);
                                }
                            }

                            @Override
                            public void showEmptyError(String msg) {
                                loadState.postValue(LoadState.SUCCESS);
                                /*
                                 * 刷新过后如果没有数据 显示 空布局
                                 */
                                if (pullToRefresh) {
                                    getAdapter().getData().clear();
                                    getAdapter().notifyDataSetChanged();
                                } else {
                                    if (refreshLayout != null) {
                                        refreshLayout.finishLoadMoreWithNoMoreData();
                                    }
                                    loadStatus.setValue(LoadMoreNoDataStatus.LOAD_MORE_WITH_NO_MORE_DATA);
                                }
                            }
                        }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        refreshLayout = null;
        adapter = null;
    }

    public BaseQuickAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(BaseQuickAdapter adapter) {
        this.adapter = adapter;
        this.adapter.setEmptyView(LayoutInflater.from(getApplication()).inflate(R.layout.view_no_data, null));
    }

    public SmartRefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    public void setRefreshLayout(SmartRefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
        refreshLayout.setOnRefreshListener(refresh -> getAllData(true));
        refreshLayout.setOnLoadMoreListener(refresh -> {
            getAllData(false);
        });
    }

    /**
     * 处理分页数据
     *
     * @param pullToRefresh pullToRefresh 是否是下拉刷新(即：第一次加载列表)  false 是加载更多
     */
    public int handlePaging(boolean pullToRefresh) {
        if (pullToRefresh) {
            return pageFrom = 1;
        } else {
            return pageFrom += 1;
        }
    }
}