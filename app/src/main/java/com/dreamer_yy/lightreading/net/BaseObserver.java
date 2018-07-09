package com.dreamer_yy.lightreading.net;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Dreamer__YY on 2018/6/5.
 */

public abstract class BaseObserver<T> implements Observer<T> {

    public abstract void onSuccess(T t);
    public abstract void onFaild(Throwable e);

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        onFaild(e);
    }

    @Override
    public void onComplete() {

    }
}
