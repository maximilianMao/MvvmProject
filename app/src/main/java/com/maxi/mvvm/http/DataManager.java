package com.maxi.mvvm.http;

import com.maxi.mvvm.bean.FindShipBean;
import com.maxi.mvvm.bean.HuoPanListBean;
import com.maxi.mvvm.bean.base.BaseResponseTypeA;
import com.maxi.mvvm.bean.base.BaseResponseTypeB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by maxi on 2021/9/14.
 */
public class DataManager {

    /**
     * 货盘列表
     * @return
     */
    public static Observable<BaseResponseTypeB<List<HuoPanListBean>>> getHuopanList(int pageFrom, int pageSize,
                                                                                    int status, String searchType, String goodsType,
                                                                                    String departureArea, String departurePort,
                                                                                    String arrivalArea, String arrivalPort,
                                                                                    String goodsTypeArray) {
        Map<String,Object> params = new HashMap<>();
        params.put("pageFrom",pageFrom);
        params.put("pageSize",pageSize);
        params.put("status",status);
        params.put("searchType",searchType);
        params.put("goodsType",goodsType);
        params.put("departureArea",departureArea);
        params.put("departurePort",departurePort);
        params.put("arrivalArea",arrivalArea);
        params.put("arrivalPort",arrivalPort);
        params.put("goodsTypeArray",goodsTypeArray);
        return RetrofitHelper.getDefault().getHuopanList(params);
    }

    /**
     * 找船单列表
     */
    public static Observable<BaseResponseTypeA<List<FindShipBean>>> getFindShip(String shipPort,
                                                                                String freeTime,
                                                                                String id,
                                                                                int pageSize,
                                                                                int pageFrom) {
        String userId = "";
//        if (MyApplication.userInfoDataBean != null) {
//            userId = String.valueOf(MyApplication.userInfoDataBean.getDatas().getUserId()) == null ? "" : String.valueOf(MyApplication.userInfoDataBean.getDatas().getUserId());
//        }
        Map<String,Object> params = new HashMap<>();
        params.put("pageFrom",pageFrom);
        params.put("pageSize",pageSize);
        params.put("userId",userId);
        params.put("id",id);
        params.put("freeTime",freeTime);
        params.put("shipPort",shipPort);
        return RetrofitHelper.getDefault().getFindShip(params);
    }
}