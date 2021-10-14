package com.maxi.mvvm.ui.fragment.home;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;

import com.maxi.mvvm.base.BaseViewModel;
import com.maxi.mvvm.base.ToolbarViewModel;

import org.jetbrains.annotations.NotNull;

/**
 * Created by maxi on 2021/9/13.
 */
public class HomeViewModel extends ToolbarViewModel {
    public HomeViewModel(@NonNull @NotNull Application application) {
        super(application);
    }
    public void initToolbar(){
        setTitleText("船舶定位");
    }
}