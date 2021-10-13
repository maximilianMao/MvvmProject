package com.maxi.mvvm.http;


import io.reactivex.functions.Consumer;

/**
 * Created by maxi on 2021/9/14.
 */
public abstract class BaseConsumer<T> implements Consumer<T> {

    @Override
    public void accept(T t) throws Exception {
        success(t);
    }

    public abstract void success(T t);
}