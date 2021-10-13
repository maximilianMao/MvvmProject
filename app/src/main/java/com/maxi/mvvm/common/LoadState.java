package com.maxi.mvvm.common;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by maxi on 2021/9/3.
 */
@IntDef({LoadState.LOADING,
        LoadState.ERROR,
        LoadState.NO_DATA,
        LoadState.NO_NETWORK,
        LoadState.SUCCESS
})
@Retention(RetentionPolicy.SOURCE)
public @interface LoadState {
    /**
     * 加载中
     */
    int LOADING = 0;

    /**
     * 没有网络
     */
    int NO_NETWORK = 1;

    /**
     * 没有数据
     */
    int NO_DATA = 2;

    /**
     * 加载出错
     */
    int ERROR = 3;

    /**
     * 加载成功
     */
    int SUCCESS = 4;

}
