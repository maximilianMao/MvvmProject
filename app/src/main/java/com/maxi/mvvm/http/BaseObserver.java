package com.maxi.mvvm.http;

import com.maxi.mvvm.http.exception.NoDataException;
import com.maxi.mvvm.http.exception.NoNetException;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by maxi on 2021/10/14.
 */
public abstract class BaseObserver<T> implements Observer<T> {
    private WeakReference<HttpLiveData> datas;

    public BaseObserver() {
    }

    public BaseObserver(HttpLiveData datas) {
        this.datas = new WeakReference<HttpLiveData>(datas);
    }

    @Override
    public void onSubscribe(@NotNull Disposable d) {
        if(datas != null && datas.get() != null) {
            datas.get().setState(HttpLiveData.HttpState.STATE_START);
        }
    }

    @Override
    public void onNext(@NotNull T t) {
        onSuccess(t);
    }

    @Override
    public void onError(@NotNull Throwable t) {
        if (t instanceof NoNetException) {
            showError("暂无网络");
            return;
        }
        if (t instanceof NoDataException) {
            showEmptyError(t.getMessage());
            return;
        }
        showError(t.getMessage());
    }

    @Override
    public void onComplete() {
        if(datas != null && datas.get() != null) {
            datas.get().setState(HttpLiveData.HttpState.STATE_COMPLETE);
        }
    }

    public abstract void onSuccess(@NotNull T t);

    public abstract void showError(String msg);

    public void showEmptyError(String msg) {
        showError(msg);//默认数据空走showError 其他情况重写此方法
    }
}