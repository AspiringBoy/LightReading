package com.dreamer_yy.lightreading.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.ScrollView;

/**
 * Created by Dreamer__YY on 2018/8/20.
 */

public class DragBackLayout extends ViewGroup {

    private Context mContext;
    //是否允许滑动返回
    private boolean enablePullBack;
    private float mLastRawX;
    private float mLastRawY;
    private float lastX;
    private float lastY;
    private float mCurRawX;
    private float mCurRawY;
    private View mTarget;
    private View scrollChild;
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

    private enum DragDirectMode {
        EDGE,
        VERCITAL, //竖直滑动
        HORIZONTAL //水平滑动
    }

    private enum DragEdge {
        LEFT,//拖动左边缘(向右滑动view)

        TOP,//拖动上边缘(向下滑动view)

        RIGHT,//拖动右边缘(向左滑动view)

        BOTTOM//拖动下边缘(向上滑动view)
    }

    public DragBackLayout(Context context) {
        this(context, null);
    }

    public DragBackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
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

    private void checkPullEnable() {
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
            getChildAt(0).layout(childLeft, childTop, childRight, childBottom);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() > 1) {
            throw new IllegalStateException("DragBackLayout content must contains only one direct child.");
        }
        if (getChildCount() > 0) {
            int measureWidth = MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY);
            int measureHeight = MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY);
            getChildAt(0).measure(measureWidth, measureHeight);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
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
        boolean handled = false;
        boolean intercepted = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = ev.getRawX();
                lastY = ev.getRawY();
                handled = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float offsetX = Math.abs(ev.getRawX() - lastX);
                float offsetY = Math.abs(ev.getRawY() - lastY);
                if (offsetY < offsetX) {//不拦截
                    handled = true;
                }
                lastX = ev.getRawX();
                lastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                handled = false;
                break;
        }
        if (!handled) {
            intercepted = viewDragHelper.shouldInterceptTouchEvent(ev);
        } else viewDragHelper.cancel();
        ensureTarget();
        return intercepted ? intercepted : super.onInterceptTouchEvent(ev);
    }

    private void ensureTarget() {
        if (mTarget == null) {
            if (getChildCount() > 1) {
                throw new IllegalStateException("DragBackLayout must contains only one content.");
            }
            if (getChildCount() > 0) {
                View child = getChildAt(0);
                mTarget = child;
                if (scrollChild == null && mTarget != null) {
                    if (mTarget instanceof ViewGroup) {
                        findScrollView((ViewGroup) mTarget);
                    } else {
                        scrollChild = mTarget;
                    }
                }
            }
        }
    }

    private void findScrollView(ViewGroup target) {
        scrollChild = target;
        if (target.getChildCount() > 0) {
            for (int i = 0; i < target.getChildCount(); i++) {
                View child = target.getChildAt(i);
                if (child instanceof AbsListView || child instanceof ScrollView || child instanceof WebView || child instanceof ViewPager) {
                    scrollChild = child;
                    return;
                }
            }
        }

    }

    /**
     * 获取view滑动范围
     * @return
     */
    private int getDragRange() {
        switch (dragEdge) {
            case TOP:
            case BOTTOM:
                return verticalDragRange;
            case LEFT:
            case RIGHT:
                return horizontalDragRange;
            default:
                return verticalDragRange;
        }
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private class ViewDragHelperCallBack extends ViewDragHelper.Callback {

        /**
         * 捕获要滑动的view
         * @param child : 拖动的view
         * @param pointerId
         * @return
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mTarget && enablePullBack;
        }

        /**
         * 拖动状态监听
         * @param state : view拖动状态（0 : 静止状态；1 : 拖动状态； 2 : 放手后自由滑动状态）
         */
        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
        }

        /**
         * view位置变化监听
         * @param changedView
         * @param left : view当前左边缘位置
         * @param top : view当前上边缘位置
         * @param dx : view在水平方向上的位移变化量(当前left - 原始left)
         * @param dy : view在竖直方向上的位移变化量
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
        }

        /**
         * 释放view触发
         * @param releasedChild : 释放的view
         * @param xvel : 释放时view在X方向上的速度
         * @param yvel : 释放时view在Y方向上的速度
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
        }

        /**
         * 设置view在水平方向的拖动范围
         * @param child
         * @return
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return horizontalDragRange;
        }

        /**
         * 设置view在竖直方向上的拖动范围
         * @param child
         * @return
         */
        @Override
        public int getViewVerticalDragRange(View child) {
            return verticalDragRange;
        }

        /**
         * 设置view拖动之后的水平方向位置
         * @param child
         * @param left : view当前左边缘位置
         * @param dx : view在水平方向的位移量
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return super.clampViewPositionHorizontal(child, left, dx);
        }

        /**
         * 设置view拖动之后的竖直方向位置
         * @param child
         * @param top : view当前上边缘位置
         * @param dy : view在竖直方向的位移量
         * @return
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            int result = 0;
            if (dragDirectMode == DragDirectMode.VERCITAL) {
                if (enablePullBack && top > 0) {

                }
            }
            switch (dragEdge) {
                case RIGHT:
                case LEFT:

                    break;
                case BOTTOM:
                case TOP:

                    break;
            }
            return super.clampViewPositionVertical(child, top, dy);
        }
    }
}
