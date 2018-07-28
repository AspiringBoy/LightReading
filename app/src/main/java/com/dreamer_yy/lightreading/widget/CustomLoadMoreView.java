package com.dreamer_yy.lightreading.widget;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.dreamer_yy.lightreading.R;

/**
 * Created by Dreamer__YY on 2018/7/28.
 */

public class CustomLoadMoreView extends LoadMoreView {
    @Override
    public int getLayoutId() {
        return R.layout.load_more_view;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return 0;
    }

    @Override
    public boolean isLoadEndGone() {
        return true;
    }
}
