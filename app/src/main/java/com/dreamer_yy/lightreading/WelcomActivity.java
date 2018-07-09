package com.dreamer_yy.lightreading;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamer_yy.lightreading.base.BaseActivity;
import com.dreamer_yy.lightreading.component.ApplicationComponent;
import com.dreamer_yy.lightreading.utils.ImageLoaderUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Dreamer__YY on 2018/5/10.
 */

public class WelcomActivity extends BaseActivity {

    @BindView(R.id.splash_ad_ignor_tv)
    TextView adIgnorTv;
    @BindView(R.id.welcom_ad_img)
    ImageView welcomAd;
    @BindView(R.id.gif_iv)
    GifImageView gifImageView;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public int getContentLayout() {
        return R.layout.activity_welcom;
    }

    @Override
    public void initInjector(ApplicationComponent applicationComponent) {

    }

    @Override
    public void initData() {

    }

    @Override
    public void bindView(View view, Bundle saveInstanceState) {
        final GifDrawable gifDrawable = (GifDrawable) gifImageView.getDrawable();
//        gifDrawable.setLoopCount(1);
        gifImageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                gifDrawable.start();
            }
        }, 100);

        //必应每日壁纸 来源于 https://www.dujin.org/fenxiang/jiaocheng/3618.html
        ImageLoaderUtils.loadImage(this, "http://api.dujin.org/bing/1920.php", welcomAd);
        mCompositeDisposable.add(getIntervalOb(4).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                adIgnorTv.setText("跳过 4");
            }
        }).subscribeWith(new DisposableObserver<Integer>() {
            @Override
            public void onNext(Integer o) {
                adIgnorTv.setText("跳过 " + o);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                toMain();
            }
        }));
    }

    private void toMain() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private Observable<Integer> getIntervalOb(int countTime) {
        if (countTime < 0) countTime = 0;
        final int finalCountTime = countTime;
        Observable<Integer> observable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(Long aLong) throws Exception {
                        return finalCountTime - aLong.intValue();
                    }
                })
                .take(countTime);
        return observable;
    }

    @OnClick(R.id.splash_ad_ignor_tv)
    public void onClick(View view) {
        toMain();
    }

    @Override
    public void onReTry() {

    }

    @Override
    protected void onDestroy() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
        super.onDestroy();
    }

}
