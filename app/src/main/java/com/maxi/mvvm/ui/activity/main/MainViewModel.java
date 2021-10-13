package com.maxi.mvvm.ui.activity.main;

import android.app.Application;

import androidx.annotation.NonNull;

import com.maxi.mvvm.base.BaseViewModel;

import org.jetbrains.annotations.NotNull;

/**
 * Created by maxi on 2021/9/3.
 */
public class MainViewModel extends BaseViewModel {
    public MainViewModel(@NonNull @NotNull Application application) {
        super(application);
    }
}