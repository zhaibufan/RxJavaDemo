package com.example.zhai.rxjavademo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Observable<Object> observable = RxBus.getInstance().register(TAG);
        observable.subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                Log.d(TAG, "result="+o.toString());
            }
        });
    }

    // 创建观察者
    Observer observer = new Observer() {
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
    };

    /**
     * create
     * @param view
     */
    public void test1(View view) {
        // 创建被观察者
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<String> observableEmitter) throws Exception {
                Thread.sleep(2000);
                Log.d(TAG, "发送数据了");
                observableEmitter.onNext("hello create");
            }
        });
        observable.subscribeOn(Schedulers.io());
        observable.observeOn(AndroidSchedulers.mainThread());

        // 订阅
        observable.subscribe(observer);
    }

    /**
     * just
     * @param view
     */
    public void test2(View view) {
        //使用just( )，将为你创建一个Observable并自动为你调用onNext( )发射数据
        Observable<String> observable = Observable.just("hello just");
        observable.subscribe(observer);
    }

    /**
     * fromIterable
     * @param view
     */
    public void test3(View view) {
        List<String> list = new ArrayList<>();
        for(int i =0;i<10;i++){
            list.add("Hello fromIterable "+i);
        }
        // 使用fromIterable()，遍历集合，发送每个item。相当于多次回调onNext()方法，每次传入一个item
        Observable<String> observable = Observable.fromIterable(list);
        observable.subscribe(observer);
    }

    /**
     * defer
     * @param view
     */
    public void test4(View view) {
        //当观察者订阅时，才创建Observable，并且针对每个观察者创建都是一个新的Observable
        Observable<String> observable = Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                return Observable.just("hello defer");
            }
        });
        observable.subscribe(observer);
    }

    /**
     * interval
     * @param view
     */
    public void test5(View view) {
        //创建一个按固定时间间隔发射整数序列的Observable，可用作定时器。即按照固定2秒一次调用onNext()方法。
        Observable<Long> observable = Observable.interval(2, TimeUnit.SECONDS);
        observable.subscribe(observer);
    }

    public void test6(View view) {
        Observable.just("hello").subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "s="+s);
            }
        });
    }

    public void test7(View view) {
        Observable
                .create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<String> observableEmitter) throws Exception {
                observableEmitter.onNext("hello custom");
            }
        })
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        Log.d(TAG, "doOnNext="+s);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        Log.d(TAG, "subscribe="+s);
                    }
                });
    }

    public void test8(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }
}
