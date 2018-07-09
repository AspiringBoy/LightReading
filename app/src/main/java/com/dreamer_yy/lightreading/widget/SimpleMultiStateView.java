package com.dreamer_yy.lightreading.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.dreamer_yy.lightreading.R;

/**
 * Created by Dreamer__YY on 2018/5/7.
 */

public class SimpleMultiStateView extends BaseMultiStateView {

    private int resIdEmpty,resIdNonet,resIdFail,resIdLoading;
    private int mTargetState = -1;
    private long mLoadingStartTime = -1;
    private static final int MIN_SHOW_TIME = 400; // ms
    private static final int MIN_DELAY = 600; // ms

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            setViewState(mTargetState);
            mLoadingStartTime = -1;
            mTargetState = -1;
        }
    };

    public SimpleMultiStateView(@NonNull Context context) {
        super(context);
    }

    public SimpleMultiStateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleMultiStateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.msv_SimpleMultiStateView);
        resIdEmpty = typedArray.getResourceId(R.styleable.msv_SimpleMultiStateView_msv_emptyView, -1);
        resIdLoading = typedArray.getResourceId(R.styleable.msv_SimpleMultiStateView_msv_loadingView, -1);
        resIdFail = typedArray.getResourceId(R.styleable.msv_SimpleMultiStateView_msv_failView, -1);
        resIdNonet = typedArray.getResourceId(R.styleable.msv_SimpleMultiStateView_msv_nonetView, -1);
        typedArray.recycle();
        if (typedArray != null) {
            if (resIdEmpty != -1) {
                addViewForState(BaseMultiStateView.STATE_EMPTY, resIdEmpty);
            }
            if (resIdFail != -1) {
                addViewForState(BaseMultiStateView.STATE_FAILD, resIdFail);
            }
            if (resIdLoading != -1) {
                addViewForState(BaseMultiStateView.STATE_LOADING, resIdLoading);
            }
            if (resIdNonet != -1) {
                addViewForState(BaseMultiStateView.STATE_NONET, resIdNonet);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        removeCallbacks(mRunnable);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(mRunnable);
    }

    @Override
    public void setViewState(int state) {
        if (getViewState() == STATE_LOADING && state != STATE_LOADING) {
            long diff = System.currentTimeMillis() - mLoadingStartTime;
            if (diff < MIN_SHOW_TIME) {
                mTargetState = state;
                postDelayed(mRunnable, MIN_DELAY);
            } else {
                super.setViewState(state);
            }
            return;
        } else if (state == STATE_LOADING) {
            mLoadingStartTime = System.currentTimeMillis();
        }
        super.setViewState(state);
    }

    public SimpleMultiStateView setEmptyLayout(int resId){
        resIdEmpty = resId;
        addViewForState(STATE_EMPTY,resId);
        return this;
    }

    public SimpleMultiStateView setNonetLayout(int resId){
        resIdNonet = resId;
        addViewForState(STATE_NONET,resId);
        return this;
    }

    public SimpleMultiStateView setFaildLayout(int resId){
        resIdFail = resId;
        addViewForState(STATE_FAILD,resId);
        return this;
    }

    public SimpleMultiStateView setLoadingLayout(int resId){
        resIdLoading = resId;
        addViewForState(STATE_LOADING,resId);
        return this;
    }

    public SimpleMultiStateView build(){
        showLoadingView();
        return this;
    }

    /**
     * 显示进度页
     */
    public void showLoadingView() {
        setViewState(BaseMultiStateView.STATE_LOADING);
    }

    /**
     * 显示错误页
     */
    public void showErrorView() {
        if (getViewState() != BaseMultiStateView.STATE_CONTENT) {
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setViewState(BaseMultiStateView.STATE_FAILD);
                }
            }, 100);
        }

    }

    /**
     * 无数据时
     */
    public void showEmptyView() {
        if (getViewState() != BaseMultiStateView.STATE_CONTENT) {
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setViewState(BaseMultiStateView.STATE_EMPTY);
                }
            }, 100);
        }

    }

    /**
     * 无网络时
     */
    public void showNoNetView() {
        if (getViewState() != BaseMultiStateView.STATE_CONTENT) {
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setViewState(BaseMultiStateView.STATE_NONET);
                }
            }, 100);

        }

    }

    /**
     * 显示内容
     */
    public void showContent() {

        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                setViewState(BaseMultiStateView.STATE_CONTENT);
            }
        }, 100);
    }

}
