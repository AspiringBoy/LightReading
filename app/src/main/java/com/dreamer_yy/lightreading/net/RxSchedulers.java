package com.dreamer_yy.lightreading.net;

import com.trello.rxlifecycle2.LifecycleTransformer;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 通用的Rx线程转换类
 * 参考:http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0819/3327.html
 * Created by Dreamer__YY on 2018/5/28.
 *
 */

public class RxSchedulers {

   static final ObservableTransformer observableTransformer = new ObservableTransformer() {
        @Override
        public ObservableSource apply(Observable upstream) {
            return upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

   public static <T>ObservableTransformer<T,T> applySchedulers(){
       return ((ObservableTransformer<T, T>) observableTransformer);
   }

}
