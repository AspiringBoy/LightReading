package com.dreamer_yy.lightreading.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.dreamer_yy.lightreading.R;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by Dreamer__YY on 2018/8/28.
 */

public class RefreshHeader extends FrameLayout implements PtrUIHandler {
    public RefreshHeader(@NonNull Context context) {
        this(context,null);
    }

    public RefreshHeader(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RefreshHeader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.refresh_header_view,this,true);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        Log.d("Dreamer__YY:", "onUIReset: ");
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        Log.d("Dreamer__YY:", "onUIRefreshPrepare: ");
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        Log.d("Dreamer__YY:", "onUIRefreshBegin: ");
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        Log.d("Dreamer__YY:", "onUIRefreshComplete: ");
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {

    }
}
