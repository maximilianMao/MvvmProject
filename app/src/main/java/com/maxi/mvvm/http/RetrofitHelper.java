package com.maxi.mvvm.http;

import com.blankj.utilcode.util.NetworkUtils;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;
import com.maxi.mvvm.BuildConfig;
import com.maxi.mvvm.http.exception.NoNetException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by maxi on 2021/9/3.
 */
public class RetrofitHelper {

    public static ApiService service;

//    public static String consignUrl = BuildConfig.LOG_DEBUG ? "https://test.oceangreate.com/df/contracth5/" : "https://tms.oceangreate.com/df/contracth5/";//合同baseUrl
//
//    public static String baseWebUrl = "https://tms.oceangreate.com/df/prot/";//内置浏览器baseUrl
//
//    public static String apkUrl = "https://tms.oceangreate.com/df/download/app/driver-latest.apk";//安装包最新下载地址
//
//    public static final String BASE_URL = BuildConfig.LOG_DEBUG ? "https://test-service.oceangreate.com/df/" : "https://tms-service.oceangreate.com/df/";

    //dev
    public static String BASE_URL = "http://dev.oceangreate.com/df-service/";
    public static String consignUrl = "https://dev.oceangreate.com/df/contracth5/";
    public static String apkUrl = "https://df-dev.oss-cn-hangzhou.aliyuncs.com/driver-latest.apk";

    //连接超时时间
    private static final int CONNECT_TIME_OUT = 10 * 1000;
    //读取超时时间
    private static final int READ_TIME_OUT = 10 * 1000;
    //写入超时时间
    private static final int WRITE_TIME_OUT = 10 * 1000;

    public static ApiService getDefault() {
        if (service == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS);
            builder.readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS);
            builder.writeTimeout(WRITE_TIME_OUT, TimeUnit.MILLISECONDS);
            if (BuildConfig.DEBUG) {
                //打印网络请求日志
                LoggingInterceptor httpLoggingInterceptor = new LoggingInterceptor.Builder()
                        .loggable(BuildConfig.DEBUG)
                        .setLevel(Level.BASIC)
                        .log(Platform.INFO)
                        .request("Request")
                        .response("Response")
                        .build();
                builder.addInterceptor(httpLoggingInterceptor);
            }
            builder.addInterceptor(new Interceptor() {
                @NotNull
                @Override
                public Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                    if (NetworkUtils.isConnected()) {
                        return chain.proceed(chain.request());
                    } else {
                        throw new NoNetException();
                    }
                }
            });
            service = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(builder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(ApiService.class);
        }
        return service;
    }
}