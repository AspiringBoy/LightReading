package com.dreamer_yy.lightreading.ui.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dreamer_yy.lightreading.R;
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
        addItemType(NewsDetail.ItemBean.TYPE_DOC_TITLEIMG, R.layout.item_detail_doc);
        addItemType(NewsDetail.ItemBean.TYPE_DOC_SLIDEIMG, R.layout.item_detail_doc_slideimg);
        addItemType(NewsDetail.ItemBean.TYPE_ADVERT_TITLEIMG, R.layout.item_detail_advert);
        addItemType(NewsDetail.ItemBean.TYPE_ADVERT_SLIDEIMG, R.layout.item_detail_advert_slideimg);
        addItemType(NewsDetail.ItemBean.TYPE_ADVERT_LONGIMG, R.layout.item_detail_advert_longimage);
        addItemType(NewsDetail.ItemBean.TYPE_SLIDE, R.layout.item_detail_slide);
        addItemType(NewsDetail.ItemBean.TYPE_PHVIDEO, R.layout.item_detail_phvideo);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsDetail.ItemBean item) {

    }
}
