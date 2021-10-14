package com.maxi.mvvm.http;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.maxi.mvvm.ui.widget.CommonDialog;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by maxi on 2021/10/14.
 */
public class HttpLiveData<T> {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            HttpState.STATE_START, HttpState.STATE_COMPLETE
    })
    public @interface HttpState {
        int STATE_START = 0;//开始请求
        int STATE_COMPLETE = 1;//请求完成
    }

    private MutableLiveData<T> mldSuccess = new MutableLiveData<>();
    private MutableLiveData<String> mldError = new MutableLiveData<>();
    private MutableLiveData<Integer> mldState = new MutableLiveData<>();

    public void setSuccess(T t) {
        mldSuccess.setValue(t);
    }

    public void postSuccess(T t) {
        mldSuccess.postValue(t);
    }

    public void setError(String t) {
        mldError.setValue(t);
    }

    public void postError(String t) {
        mldError.postValue(t);
    }

    public void setState(@HttpState int t) {
        mldState.setValue(t);
    }

    public void postState(@HttpState int t) {
        mldState.postValue(t);
    }

    public void observe(LifecycleOwner lifecycleOwner, com.maxi.mvvm.http.HttpObserve<T> httpObserve) {
        mldSuccess.observe(lifecycleOwner, new Observer<T>() {
            @Override
            public void onChanged(T t) {
                httpObserve.onSuccess(t);
            }
        });
        mldError.observe(lifecycleOwner, new Observer<String>() {
            @Override
            public void onChanged(String t) {
                httpObserve.onError(t);
            }
        });
        mldState.observe(lifecycleOwner, new Observer<Integer>() {
            @Override
            public void onChanged(Integer t) {
                switch (t) {
                    case HttpState.STATE_START:
                        httpObserve.onStart();
                        break;
                    case HttpState.STATE_COMPLETE:
                        httpObserve.onComplete();
                        break;
                }
            }
        });
    }
}