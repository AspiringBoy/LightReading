package com.dreamer_yy.lightreading.widget;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dreamer_yy.lightreading.R;
import com.dreamer_yy.lightreading.utils.ContextUtils;

/**
 * Created by Dreamer__YY on 2018/5/9.
 */

public class BottomBarTab extends LinearLayout {

    private Context mContext;
    private ImageView mIcon;
    private TextView mTabTitle;
    private int tabPosition = -1;

    public BottomBarTab(Context context, @DrawableRes int resIcon, String tabTitle) {
        this(context,null,resIcon,tabTitle);
    }

    public BottomBarTab(Context context, @Nullable AttributeSet attrs,@DrawableRes int resIcon, String tabTitle) {
        this(context, attrs,0,resIcon,tabTitle);
    }

    public BottomBarTab(Context context, @Nullable AttributeSet attrs, int defStyleAttr,@DrawableRes int resIcon, String tabTitle) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(resIcon,tabTitle);
    }

    private void init(int resIcon, String tabTitle) {
        setOrientation(VERTICAL);
        mIcon = new ImageView(mContext);
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        LayoutParams iconParams = new LayoutParams(size, size);
        iconParams.gravity = Gravity.CENTER_HORIZONTAL;
        iconParams.topMargin = ContextUtils.dip2px(mContext,2.5f);
        mIcon.setImageResource(resIcon);
        mIcon.setLayoutParams(iconParams);

        mTabTitle = new TextView(mContext);
        LayoutParams titleParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleParams.gravity = Gravity.CENTER_HORIZONTAL;
        titleParams.topMargin = ContextUtils.dip2px(mContext,2.5f);
        titleParams.bottomMargin = ContextUtils.dip2px(mContext,2.5f);
        mTabTitle.setTextColor(ContextCompat.getColor(mContext, R.color.tab_unselect));
        mTabTitle.setTextSize(ContextUtils.dip2px(mContext,10f));
        mTabTitle.setText(tabTitle);
        mTabTitle.setLayoutParams(titleParams);

        addView(mIcon);
        addView(mTabTitle);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected) {
            mIcon.setColorFilter(ContextCompat.getColor(mContext,R.color.tab_select));
            mTabTitle.setTextColor(ContextCompat.getColor(mContext,R.color.tab_select));
        }else {
            mIcon.setColorFilter(ContextCompat.getColor(mContext,R.color.tab_unselect));
            mTabTitle.setTextColor(ContextCompat.getColor(mContext,R.color.tab_unselect));
        }
    }

    public int getTabPosition() {
        return tabPosition;
    }

    public void setTabPosition(int tabPosition) {
        this.tabPosition = tabPosition;
        if (tabPosition == 0) {
            setSelected(true);
        }
    }
}
