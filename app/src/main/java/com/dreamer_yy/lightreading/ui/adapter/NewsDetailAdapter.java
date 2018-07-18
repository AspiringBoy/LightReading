package com.dreamer_yy.lightreading.ui.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dreamer_yy.lightreading.bean.NewsDetail;

import java.util.List;

/**
 * Created by Dreamer__YY on 2018/7/18.
 */

public class NewsDetailAdapter extends BaseMultiItemQuickAdapter<NewsDetail.ItemBean,BaseViewHolder> {

    private final Context mContext;

    public NewsDetailAdapter(List<NewsDetail.ItemBean> data, Context context) {
        super(data);
        mContext = context;

    }

    @Override
    protected void convert(BaseViewHolder helper, NewsDetail.ItemBean item) {

    }
}
