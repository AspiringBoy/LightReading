package com.dreamer_yy.lightreading.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.dreamer_yy.lightreading.R;

import java.util.Date;

/**
 * Created by Dreamer__YY on 2018/4/28.
 */

public class BaseMultiStateView extends FrameLayout {

    public static final int STATE_CONTENT = 10001;
    public static final int STATE_EMPTY = 10002;
    public static final int STATE_LOADING= 10003;
    public static final int STATE_FAILD= 10004;
    public static final int STATE_NONET= 10005;
    private SparseIntArray mLayoutStateArray = new SparseIntArray();
    private SparseArray<View> mViewStateArray = new SparseArray<>();
    private View mContentView;
    private int mCurrentState = STATE_CONTENT;
    private OnReloadListener onReloadListener;
    private OnInflateListener onInflateListener;

    public BaseMultiStateView(@NonNull Context context) {
        this(context,null);
    }

    public BaseMultiStateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BaseMultiStateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child) {
        validStateView(child);
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        validStateView(child);
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int width, int height) {
        validStateView(child);
        super.addView(child, width, height);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        validStateView(child);
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        validStateView(child);
        super.addView(child, index, params);
    }

    private void validStateView(View child) {
        if (isValidContentView(child)){
            Log.d("Dreamer__YY:", "validStateView: yy");
            mContentView = child;
            mViewStateArray.put(STATE_CONTENT,child);
        } else if (mCurrentState != STATE_CONTENT) {
            mContentView.setVisibility(GONE);
        }
    }

    private boolean isValidContentView(View view) {
        if (mContentView == null) {
            for (int i = 0; i < mViewStateArray.size(); i++) {
                if (mViewStateArray.indexOfValue(view) != -1) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 改变视图状态
     * @param state(状态类型)
     */
    public void setViewState(int state){
        if (getCurrentView() == null) return;
        if (mCurrentState != state) {
            View view = getView(state);
            getCurrentView().setVisibility(GONE);
            mCurrentState = state;
            if (view != null) {
                view.setVisibility(VISIBLE);
            }else {
                int layoutId = mLayoutStateArray.get(state);
                if (layoutId == 0) return;
                view = LayoutInflater.from(getContext()).inflate(layoutId,this,false);
                addView(view);
                mViewStateArray.put(state,view);
                view.setVisibility(VISIBLE);
                if (state == STATE_FAILD) {
                    View retry = view.findViewById(R.id.retry_view);
                    if (retry != null) {
                        retry.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (onReloadListener != null) {
                                    onReloadListener.onReload();
                                }
                            }
                        });
                    }
                }
                if (onInflateListener != null) {
                    onInflateListener.onInflate(state,view);
                }
            }
        }
    }

    /**
     * 获取当前view
     * @return
     */
    public View getCurrentView(){
        if (mCurrentState == -1) return null;
        View view = getView(mCurrentState);
        if (view == null && mCurrentState == STATE_CONTENT) {
            throw new NullPointerException("content is null");
        } else if (view == null) {
            throw new NullPointerException("current view is null  state ="+mCurrentState);
        }
        return getView(mCurrentState);
    }

    /**
     * 根据状态获取view
     * @param state 状态
     * @return 指定状态的view
     */
    public View getView(int state){
        return mViewStateArray.get(state);
    }

    /**
     * 获取当前状态值
     * @return
     */
    public int getViewState(){
        return mCurrentState;
    }

    public void addViewForState(int state,int resId){
        mLayoutStateArray.put(state,resId);
    }

    /**
     * 重新加载监听
     */
    public interface OnReloadListener{
        void onReload();
    }

    /**
     *
     */
    public interface OnInflateListener{
        void onInflate(int state,View view);
    }

    public OnReloadListener getOnReloadListener() {
        return onReloadListener;
    }

    public void setOnReloadListener(OnReloadListener onReloadListener) {
        this.onReloadListener = onReloadListener;
    }

    public OnInflateListener getOnInflateListener() {
        return onInflateListener;
    }

    public void setOnInflateListener(OnInflateListener onInflateListener) {
        this.onInflateListener = onInflateListener;
    }
}
