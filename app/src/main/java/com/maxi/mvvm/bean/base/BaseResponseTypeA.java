package com.maxi.mvvm.bean.base;

/**
 * Created by maxi on 2021/9/3.
 */
public class BaseResponseTypeA<T> extends BaseResponse {

    private T datas;

    public T getDatas() {
        return datas;
    }

    public void setDatas(T datas) {
        this.datas = datas;
    }
}