package com.dreamer_yy.lightreading.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Dreamer__YY on 2018/5/9.
 */

public class BottomBarView extends LinearLayout {

    private LinearLayout mTabContainers;
    private Context mContext;
    private LayoutParams mTabParams;
    private ITabSelectListener onTabSelectListener;
    private int mCurrentPos = 0;

    public BottomBarView(Context context) {
        this(context, null);
    }

    public BottomBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mTabContainers = new LinearLayout(mContext);
        setOrientation(VERTICAL);
        mTabContainers.setOrientation(HORIZONTAL);
        mTabContainers.setBackgroundColor(Color.WHITE);
        addView(mTabContainers, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mTabParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        mTabParams.weight = 1;
    }

    public BottomBarView addTabItem(final BottomBarTab tab) {
        tab.setTabPosition(mTabContainers.getChildCount());
        mTabContainers.addView(tab, mTabParams);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTabSelectListener != null) {
                    int tabPosition = tab.getTabPosition();
                    if (tabPosition == mCurrentPos) {//重复点击tab
                        onTabSelectListener.onTabReselect(tabPosition);
                    } else {
                        onTabSelectListener.onTabSelect(tabPosition, mCurrentPos);
                        onTabSelectListener.onTabUnselect(mCurrentPos);
                        tab.setSelected(true);
                        mTabContainers.getChildAt(mCurrentPos).setSelected(false);
                        mCurrentPos = tabPosition;
                    }
                }
            }
        });
        return this;
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        return new SaveState(super.onSaveInstanceState(), mCurrentPos);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SaveState ss = (SaveState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        if (ss.position != mCurrentPos) {
            mTabContainers.getChildAt(mCurrentPos).setSelected(false);
            mTabContainers.getChildAt(ss.position).setSelected(true);
        }
        mCurrentPos = ss.position;
    }

    public interface ITabSelectListener {
        void onTabSelect(int pos, int prePos);

        void onTabUnselect(int pos);

        void onTabReselect(int pos);
    }

    static class SaveState extends BaseSavedState {

        private int position;

        public SaveState(Parcel source) {
            super(source);
            position = source.readInt();
        }

        public SaveState(Parcelable superState, int position) {
            super(superState);
            this.position = position;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(position);
        }

        public static final Creator<SaveState> CREATOR = new Creator<SaveState>() {
            @Override
            public SaveState createFromParcel(Parcel source) {
                return new SaveState(source);
            }

            @Override
            public SaveState[] newArray(int size) {
                return new SaveState[size];
            }
        };
    }

    public ITabSelectListener getOnTabSelectListener() {
        return onTabSelectListener;
    }

    public void setOnTabSelectListener(ITabSelectListener onTabSelectListener) {
        this.onTabSelectListener = onTabSelectListener;
    }
}
