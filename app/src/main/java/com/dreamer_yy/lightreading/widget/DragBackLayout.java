package com.dreamer_yy.lightreading.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.ScrollView;

/**
 * 滑动返回工具类
 * 只能包含一个子view
 * 所属activity必须使用透明主题
 * <style name="Theme.Swipe.Back" parent="AppTheme">
 * <item name="android:windowIsTranslucent">true</item>
 * <item name="android:windowBackground">@android:color/transparent</item>
 * </style>
 * Created by Dreamer__YY on 2018/8/20.
 *
 */

public class DragBackLayout extends ViewGroup {

    private Context mContext;
    //是否允许滑动返回
    private boolean enablePullBack = true;
    //是否允许快速滑动返回
    private boolean enableFlingBack = true;
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
    //view最终滑动的距离
    private float draggingOffset = 0;
    //当前状态
    private int draggingState = 0;
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
    //快速滑动返回的速度的最小值
    private static final double AUTO_FINISHED_SPEED_LIMIT = 2000.0;
    private ViewDragHelper viewDragHelper;
    private DragBackListener dragBackListener;
    private float delX;
    private float delY;

    public enum DragDirectMode {
        EDGE,
        VERCITAL, //竖直滑动
        HORIZONTAL //水平滑动
    }

    public enum DragEdge {
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
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    mLastRawY = motionEvent.getRawY();
                    mLastRawX = motionEvent.getRawX();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    mCurRawY = motionEvent.getRawY();
                    mLastRawX = motionEvent.getRawX();

                    delY = Math.abs(mCurRawY - mLastRawY);
                    mLastRawY = mCurRawY;

                    delX = Math.abs(mCurRawX - mLastRawX);
                    mLastRawX = mCurRawX;
                    Log.d("Dreamer__YY:", "delX: "+delX);
                    Log.d("Dreamer__YY:", "delY: "+delY);
                    switch (dragEdge) {
                        case TOP:
                        case BOTTOM:
                            setEnablePullBack(delY > delX);
                        case LEFT:
                        case RIGHT:
                            setEnablePullBack(delY < delX);
                            break;
                    }
                }
                return false;
            }
        });
    }

    public void setEnablePullBack(boolean enablePullBack) {
        this.enablePullBack = enablePullBack;
    }

    public boolean isEnableFlingBack() {
        return enableFlingBack;
    }

    public void setEnableFlingBack(boolean enableFlingBack) {
        this.enableFlingBack = enableFlingBack;
    }

    public DragDirectMode getDragDirectMode() {
        return dragDirectMode;
    }

    public void setDragDirectMode(DragDirectMode dragDirectMode) {
        this.dragDirectMode = dragDirectMode;
        if (dragDirectMode == DragDirectMode.VERCITAL) {
            dragEdge = DragEdge.TOP;
        } else if (dragDirectMode == DragDirectMode.HORIZONTAL) {
            dragEdge = DragEdge.LEFT;
        }
    }

    public void setDragBackListener(DragBackListener dragBackListener) {
        this.dragBackListener = dragBackListener;
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
        float x = ev.getRawX();
        float y = ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = ev.getRawX();
                lastY = ev.getRawY();
                handled = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float offsetX = Math.abs(x - lastX);
                float offsetY = Math.abs(y - lastY);
                if (offsetY < offsetX) {//不拦截
                    handled = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                handled = false;
                break;
        }
        lastX = x;
        lastY = y;
        if (!handled) {
            intercepted = viewDragHelper.shouldInterceptTouchEvent(ev);
        } else viewDragHelper.cancel();
        ensureTarget();
        return intercepted ? intercepted : super.onInterceptTouchEvent(ev);

        /*boolean isIntercepted = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isIntercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltax = x - lastX;
                int deltay = y - lastY;
                if (Math.abs(deltay) > Math.abs(deltax)) {
                    isIntercepted = false;
                } else {
                    isIntercepted = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                isIntercepted = false;
                break;
        }
        lastX = x;
        lastY = y;
        boolean handled = false;
        ensureTarget();
        if (!isIntercepted) {
            handled = viewDragHelper.shouldInterceptTouchEvent(ev);
        } else {
            viewDragHelper.cancel();
        }
        return !handled ? super.onInterceptTouchEvent(ev) : handled;*/
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
     *
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
         *
         * @param child     : 拖动的view
         * @param pointerId
         * @return
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mTarget && enablePullBack;
        }

        /**
         * 拖动状态监听
         *
         * @param state : view拖动状态（0 : 静止状态；1 : 拖动状态； 2 : 放手后自由滑动状态）
         */
        @Override
        public void onViewDragStateChanged(int state) {
            if (draggingState == state) return;
            if ((draggingState == ViewDragHelper.STATE_DRAGGING || draggingState == ViewDragHelper.STATE_SETTLING) && state == ViewDragHelper.STATE_IDLE) {
                if (draggingOffset == getDragRange()) {
                    finishActivity();
                }
            }
            draggingState = state;
        }

        /**
         * view位置变化监听
         *
         * @param changedView
         * @param left        : view当前左边缘位置
         * @param top         : view当前上边缘位置
         * @param dx          : view在水平方向上的位移变化量(当前left - 原始left)
         * @param dy          : view在竖直方向上的位移变化量
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            switch (dragEdge) {
                case RIGHT:
                case LEFT:
                    draggingOffset = Math.abs(left);
                    break;
                case BOTTOM:
                case TOP:
                    draggingOffset = Math.abs(top);
                    break;
            }

            float fractionAnchor = draggingOffset / finishAnchor;
            float fractionScreen = draggingOffset / getDragRange();
            if (fractionAnchor > 1) fractionAnchor = 1;
            if (fractionScreen > 1) fractionScreen = 1;
            if (dragBackListener != null) dragBackListener.onViewPositionChanged(fractionAnchor, fractionScreen);
        }

        /**
         * 释放view触发
         *
         * @param releasedChild : 释放的view
         * @param xvel          : 释放时view在X方向上的速度
         * @param yvel          : 释放时view在Y方向上的速度
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (draggingOffset == 0 || draggingOffset == getDragRange()) {
                return;
            }
            boolean isBack = false;
            if (enableFlingBack && backBySpeed(xvel, yvel)) {
                isBack = true;
            } else if (draggingOffset >= finishAnchor) {
                isBack = true;
            } else if (draggingOffset < finishAnchor) {
                isBack = false;
            }

            int finalLeft = 0;
            int finalTop = 0;
            switch (dragEdge) {
                case TOP:
                    finalTop = isBack ? verticalDragRange : 0;
                    smoothScrollToY(finalTop);
                    break;
                case BOTTOM:
                    finalTop = isBack ? -verticalDragRange : 0;
                    smoothScrollToY(finalTop);
                    break;
                case LEFT:
                    finalLeft = isBack ? horizontalDragRange : 0;
                    smoothScrollToX(finalLeft);
                    break;
                case RIGHT:
                    finalLeft = isBack ? -horizontalDragRange : 0;
                    smoothScrollToX(finalLeft);
                    break;
            }
        }

        /**
         * 设置view在水平方向的拖动范围
         *
         * @param child
         * @return
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return horizontalDragRange;
        }

        /**
         * 设置view在竖直方向上的拖动范围
         *
         * @param child
         * @return
         */
        @Override
        public int getViewVerticalDragRange(View child) {
            return verticalDragRange;
        }

        /**
         * 设置view拖动之后的水平方向位置
         *
         * @param child
         * @param left  : view当前左边缘位置
         * @param dx    : view在水平方向的位移量
         * @return (默认返回0)
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            int result = 0;

            if (dragDirectMode == DragDirectMode.HORIZONTAL) {
                if (!canChildScrollRight() && left > 0) {
                    dragEdge = DragEdge.LEFT;
                } else if (!canChildScrollLeft() && left < 0) {
                    dragEdge = DragEdge.RIGHT;
                }
            }

            if (dragEdge == DragEdge.LEFT && !canChildScrollRight() && left > 0) {
                final int leftBound = getPaddingLeft();
                final int rightBound = horizontalDragRange;
                result = Math.min(Math.max(left, leftBound), rightBound);
            } else if (dragEdge == DragEdge.RIGHT && !canChildScrollLeft() && left < 0) {
                final int leftBound = -horizontalDragRange;
                final int rightBound = getPaddingLeft();
                result = Math.min(Math.max(left, leftBound), rightBound);
            }
            return result;
        }

        /**
         * 设置view拖动之后的竖直方向位置
         *
         * @param child
         * @param top   : view当前上边缘位置
         * @param dy    : view在竖直方向的位移量
         * @return (默认返回0)
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            int result = 0;
            if (dragDirectMode == DragDirectMode.VERCITAL) {
                if (!canChildScrollDown() && top > 0) {
                    dragEdge = DragEdge.TOP;
                } else if (!canChildScrollUp() && top < 0) {
                    dragEdge = DragEdge.BOTTOM;
                }
            }

            if (dragEdge == DragEdge.TOP && top > 0 && !canChildScrollDown()) {
                int topBound = getPaddingTop();
                int bottomBound = verticalDragRange;
                result = Math.min(Math.max(top, topBound), bottomBound);
            } else if (dragEdge == DragEdge.BOTTOM && top < 0 && !canChildScrollUp()) {
                int topBound = -verticalDragRange;
                int bottomBound = getPaddingTop();
                result = Math.min(Math.max(top, topBound), bottomBound);
            }
            return result;
        }
    }

    /**
     * 在Y方向上移动view到finalTop
     *
     * @param finalTop : view最终的上边缘位置
     */
    private void smoothScrollToY(int finalTop) {
        if (viewDragHelper.settleCapturedViewAt(0, finalTop)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * 在X方向上移动view到finalLeft
     *
     * @param finalLeft : view最终的左边缘位置
     */
    private void smoothScrollToX(int finalLeft) {
        if (viewDragHelper.settleCapturedViewAt(finalLeft, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private boolean canChildScrollRight() {
        return ViewCompat.canScrollHorizontally(scrollChild, 1);
    }

    private boolean canChildScrollLeft() {
        return ViewCompat.canScrollHorizontally(scrollChild, -1);
    }

    private boolean canChildScrollUp() {
        return ViewCompat.canScrollVertically(scrollChild, -1);
    }

    private boolean canChildScrollDown() {
        return ViewCompat.canScrollVertically(scrollChild, 1);
    }

    /**
     * 是否允许快速滑动返回
     *
     * @param xvel : X方向上的速度
     * @param yvel : Y方向上的速度
     * @return
     */
    private boolean backBySpeed(float xvel, float yvel) {
        switch (dragEdge) {
            case TOP:
            case BOTTOM:
                if (Math.abs(yvel) > Math.abs(xvel) && Math.abs(yvel) > AUTO_FINISHED_SPEED_LIMIT) {
                    return dragEdge == DragEdge.TOP ? !canChildScrollDown() : !canChildScrollUp();
                }
                break;
            case LEFT:
            case RIGHT:
                if (Math.abs(xvel) > Math.abs(yvel) && Math.abs(xvel) > AUTO_FINISHED_SPEED_LIMIT) {
                    return dragEdge == DragEdge.LEFT ? !canChildScrollRight() : !canChildScrollLeft();
                }
                break;
        }
        return false;
    }

    private void finishActivity() {
        Activity act = (Activity) getContext();
        act.finish();
        act.overridePendingTransition(0,android.R.anim.fade_out);
    }

    public interface DragBackListener {

        /**
         * view滑动比例监听.
         *
         * @param fractionAnchor 相对于滑动返回临界值的比例
         * @param fractionScreen 相对整个滑动范围的临界值
         */
        void onViewPositionChanged(float fractionAnchor, float fractionScreen);

    }

}
