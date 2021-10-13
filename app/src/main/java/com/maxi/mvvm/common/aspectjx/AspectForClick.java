package com.maxi.mvvm.common.aspectjx;

import com.maxi.mvvm.utils.DoubleClickUtils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * 点击事件一律过滤双击 如果需要双击的 需要在onClick方法上加 @AllowDoubleClick 注解
 * Created by maxi on 2021/9/14.
 */
@Aspect
public class AspectForClick {

    private boolean allowDoubleClick = false;

    @Before("execution(@com.oceangreate.df.datav.common.aspectjx.AllowDoubleClick * *(..))")
    public void beforeEnableDoubleClick(JoinPoint joinPoint) throws Throwable {
        allowDoubleClick = true;
    }

    @Around("execution(* android.view.View.OnClickListener.onClick(..))")
    public void onClickListener(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if (allowDoubleClick || !DoubleClickUtils.isFastDoubleClick()) {
            proceedingJoinPoint.proceed();
            allowDoubleClick = false;
        }
    }
}