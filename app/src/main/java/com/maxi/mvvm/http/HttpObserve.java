package com.maxi.mvvm.http;

/**
 * Created by maxi on 2021/10/14.
 */
public abstract class HttpObserve<T> {
    public abstract void onSuccess(T t);

    public void onError(String msg) {

    }

    public void onStart() {

    }

    public void onComplete() {

    }
}