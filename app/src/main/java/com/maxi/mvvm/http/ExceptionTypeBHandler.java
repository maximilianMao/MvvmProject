package com.maxi.mvvm.http;

import android.text.TextUtils;

import com.maxi.mvvm.bean.base.BaseResponseTypeB;
import com.maxi.mvvm.http.exception.NoDataException;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.functions.Function;

/**
 * Created by maxi on 2021/9/14.
 */
public class ExceptionTypeBHandler<T> implements Function<BaseResponseTypeB<T>, T> {

    @Override
    public T apply(@NotNull BaseResponseTypeB<T> baseResp) throws Exception {
        if(baseResp != null && !baseResp.isSuccess()){
            throw new Exception(baseResp.getDesc());//note 统一返回exception 后期如要区分再创建
        }
        if(baseResp.getDatas() == null || baseResp.getDatas().getList() == null ||
                (baseResp.getDatas().getList() instanceof List && ((List)baseResp.getDatas().getList()).size() <= 0)){
            throw new NoDataException(TextUtils.isEmpty(baseResp.getDesc())?"暂无数据":baseResp.getDesc());
        }
        return baseResp.getDatas().getList();
    }
}
