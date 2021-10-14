package com.maxi.mvvm.http;

import com.maxi.mvvm.bean.FindShipBean;
import com.maxi.mvvm.bean.HuoPanListBean;
import com.maxi.mvvm.bean.base.BaseResponseTypeA;
import com.maxi.mvvm.bean.base.BaseResponseTypeB;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by maxi on 2021/9/3.
 */
public interface ApiService {

    /**
     * 货盘列表
     *
     * @return
     */
    @GET("iot/roadOrder/get/v3_5_0")
    Observable<BaseResponseTypeB<List<HuoPanListBean>>> getHuopanList(@QueryMap Map<String,Object> params);

    /**
     * 找船单列表
     */
    @GET("iot/findShip/get/v3_5_0")
    Observable<BaseResponseTypeA<List<FindShipBean>>> getFindShip(@QueryMap Map<String,Object> params);

}
