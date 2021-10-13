package com.maxi.mvvm.http;

import com.maxi.mvvm.http.exception.NoDataException;
import com.maxi.mvvm.http.exception.NoNetException;

import io.reactivex.functions.Consumer;

/**
 * Created by maxi on 2021/10/9.
 */
public abstract class BaseOnErrorConsumer implements Consumer<Throwable> {
    @Override
    public void accept(Throwable t) throws Exception {
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

    public abstract void showError(String msg);

    public void showEmptyError(String msg) {
        showError(msg);//默认数据空走showError 其他情况重写此方法
    }
}