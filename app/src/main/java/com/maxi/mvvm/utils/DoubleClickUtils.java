package com.maxi.mvvm.utils;

public class DoubleClickUtils {
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {

        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 1000) {

            return true;

        }

        lastClickTime = time;

        return false;

    }
}
