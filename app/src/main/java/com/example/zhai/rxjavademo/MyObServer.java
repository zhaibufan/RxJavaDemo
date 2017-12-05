package com.example.zhai.rxjavademo;

import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Mastra on 2017/12/5.
 */

public class MyObServer implements Observer {

    private static final String TAG = "MyObServer";

    @Override
    public void onSubscribe(@NonNull Disposable disposable) {
        Log.d(TAG, "onSubscribe");
    }

    @Override
    public void onNext(@NonNull Object o) {
        Log.d(TAG, "onNext");
        Log.d(TAG, "收到的数据："+o.toString());
    }

    @Override
    public void onError(@NonNull Throwable throwable) {
        Log.d(TAG, "onError");
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete");
    }
}
