package com.maxi.mvvm.base;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

/**
 * Created by maxi on 2021/9/13.
 */
public class ToolbarViewModel<M extends BaseModel> extends BaseViewModel<M> {

    //标题文字
    public ObservableField<String> titleText = new ObservableField<>("");
    //Exit的观察者
    public ObservableInt ivExitVisibleObservable = new ObservableInt(View.GONE);
    //Back图标的观察者
    public ObservableInt ivBackVisibleObservable = new ObservableInt(View.GONE);
    //Close的观察者
    public ObservableInt tvCloseVisibleObservable = new ObservableInt(View.GONE);
    //兼容databinding，去泛型化
    public ToolbarViewModel toolbarViewModel;

    public ToolbarViewModel(@NonNull Application application) {
        this(application, null);
    }

    public ToolbarViewModel(@NonNull Application application, M model) {
        super(application, model);
        toolbarViewModel = this;
    }
    /**
     * 设置标题
     *
     * @param text 标题文字
     */
    public void setTitleText(String text) {
        titleText.set(text);
    }

    /**
     * 设置Exit隐藏/显示
     *
     * @param visibility
     */
    public void setExitTextVisible(int visibility) {
        ivExitVisibleObservable.set(visibility);
    }

    /**
     * 设置Back隐藏/显示
     *
     * @param visibility
     */
    public void setBackTextVisible(int visibility) {
        ivBackVisibleObservable.set(visibility);
    }

    /**
     * 设置Close隐藏/显示
     *
     * @param visibility
     */
    public void setCloseTextVisible(int visibility) {
        tvCloseVisibleObservable.set(visibility);
    }

    /**
     * 返回按钮的点击事件
     */
    public final View.OnClickListener backOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };
    /**
     * 关闭按钮的点击事件
     */
    public final View.OnClickListener finishOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
}