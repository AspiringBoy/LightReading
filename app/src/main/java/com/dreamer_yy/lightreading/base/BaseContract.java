package com.dreamer_yy.lightreading.base;

import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * Created by Dreamer__YY on 2018/4/24.
 */

public interface BaseContract {

    interface BasePresenter<T extends BaseContract.BaseView> {
        void attachView(T view);

        void detachView();
    }

    interface BaseView {
        //显示加载中
        void showLoading();

        //显示成功
        void showSuccess();

        //显示失败
        void showFaild();

        //重试
        void onReTry();

        //显示无数据
        void showEmpty();

        //无网络
        void noNetWork();

        //绑定生命周期
        <T> LifecycleTransformer<T> bindToLife();
    }
}
