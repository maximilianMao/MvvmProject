package com.maxi.mvvm.http.exception;

import java.io.IOException;

/**
 * 无网异常
 * Created by maxi on 2021/9/2.
 */
public class NoNetException extends IOException {
    public NoNetException() {
    }

    public NoNetException(String message) {
        super(message);
    }
}