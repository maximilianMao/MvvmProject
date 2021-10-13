package com.maxi.mvvm.base;

/**
 * Created by maxi on 2021/9/3.
 */
public class BaseModel {

    public BaseModel() {
    }

    /**
     * ViewModel销毁时清除Model，与ViewModel共消亡。Model层同样不能持有长生命周期对象
     */
    public void onCleared() {

    }
}