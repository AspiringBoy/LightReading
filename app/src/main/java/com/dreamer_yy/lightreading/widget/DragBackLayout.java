package com.dreamer_yy.lightreading.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Dreamer__YY on 2018/8/20.
 */

public class DragBackLayout extends ViewGroup {

    private Context mContext;
    //是否允许滑动返回
    private boolean enablePullBack;
    private float mLastRawX;
    private float mLastRawY;
    private float mCurRawX;
    private float mCurRawY;
    //滑动返回临界值
    private float finishAnchor = 0;
    //滑动模式
    private DragDirectMode dragDirectMode = DragDirectMode.EDGE;
    //滑动方向
    private DragEdge dragEdge = DragEdge.TOP;
    //水平方向拖动的范围
    private int horizontalDragRange;
    //垂直方向拖动的范围
    private int verticalDragRange;
    //计算返回临界值的比例
    private static final float BACK_FACTOR = 0.5f;
    private ViewDragHelper viewDragHelper;

    private enum DragDirectMode{
        EDGE,
        VERCITAL,
        HORIZONTAL
    }

    private enum DragEdge {
        LEFT,

        TOP,

        RIGHT,

        BOTTOM
    }

    public DragBackLayout(Context context) {
        this(context,null);
    }

    public DragBackLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DragBackLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        viewDragHelper = ViewDragHelper.create(this, 1f, new ViewDragHelperCallBack());
        checkPullEnable();
    }

    private void checkPullEnable(){
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mLastRawX = event.getRawX();
                        mLastRawY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mCurRawX = event.getRawX();
                        mCurRawY = event.getRawY();
                        float delX = Math.abs(mCurRawX - mLastRawX);
                        float delY = Math.abs(mCurRawY - mLastRawY);
                        mLastRawX = mCurRawX;
                        mLastRawY = mCurRawY;
                        if (dragEdge == DragEdge.TOP || dragEdge == DragEdge.TOP) {
                            setEnablePullBack(delY > delX);
                        } else if (dragEdge == DragEdge.LEFT || dragEdge == DragEdge.RIGHT) {
                            setEnablePullBack(delY < delX);
                        }
                        break;
                }
                return false;
            }
        });
    }

    public void setEnablePullBack(boolean enablePullBack) {
        this.enablePullBack = enablePullBack;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() > 0) {
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();
            int childWidth = width - getPaddingLeft() - getPaddingRight();
            int childHeight = height - getPaddingTop() - getPaddingBottom();
            int childLeft = getPaddingLeft();
            int childTop = getPaddingTop();
            int childRight = childLeft + childWidth;
            int childBottom = childTop + childHeight;
            getChildAt(0).layout(childLeft,childTop,childRight,childBottom);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        if (getChildCount() > 1) {
            throw new IllegalStateException("DragBackLayout content must contains only one direct child.");
        }
        if (getChildCount() > 0) {
            int measureWidth = MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY);
            int measureHeight = MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY);
            getChildAt(0).measure(measureWidth,measureHeight);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h,oldw,oldh);
        horizontalDragRange = w;
        verticalDragRange = h;
        switch (dragEdge) {
            case TOP:
            case BOTTOM:
                finishAnchor = finishAnchor > 0 ? finishAnchor : verticalDragRange * BACK_FACTOR;
                break;
            case LEFT:
            case RIGHT:
                finishAnchor = finishAnchor > 0 ? finishAnchor : horizontalDragRange * BACK_FACTOR;
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private class ViewDragHelperCallBack extends ViewDragHelper.Callback{

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return false;
        }
    }
}
