package com.dreamer_yy.lightreading.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.dreamer_yy.lightreading.R;
import com.dreamer_yy.lightreading.ui.adapter.NewsDelPopAdapter;
import com.flyco.dialog.widget.popup.base.BasePopup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dreamer__YY on 2018/8/8.
 */

public class NewsDelPop extends BasePopup<NewsDelPop> {

    @BindView(R.id.iv_top)
    ImageView ivTop;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_nolike)
    TextView tvNolike;
    @BindView(R.id.iv_down)
    ImageView ivDown;

    private List<String> delReasons;
    private NewsDelPopAdapter newsDelPopAdapter;
    private int position;
    private INolikeClickListener nolikeClickListener;

    public NewsDelPop(Context context) {
        super(context);
    }

    @Override
    public View onCreatePopupView() {
        View popView = View.inflate(mContext, R.layout.pop_news_del, null);
        ButterKnife.bind(this, popView);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        newsDelPopAdapter = new NewsDelPopAdapter(R.layout.news_delpop_rclv_item, delReasons);
        recyclerView.setAdapter(newsDelPopAdapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                newsDelPopAdapter.updateItems(position);
                if (newsDelPopAdapter.hasItemSelected()) {
                    tvNolike.setText("确定");
                } else tvNolike.setText("不感兴趣");
            }
        });
        return popView;
    }

    @Override
    public void setUiBeforShow() {

    }

    public NewsDelPop setBackReason(List<String> delReasons, boolean isTop,int position) {
        this.delReasons = delReasons;
        this.position = position;
        if (newsDelPopAdapter != null) {
            newsDelPopAdapter.resetSelectId();
            newsDelPopAdapter.setNewData(delReasons);
        }
        if (isTop){
            ivTop.setVisibility(View.GONE);
            ivDown.setVisibility(View.VISIBLE);
        }else {
            ivTop.setVisibility(View.VISIBLE);
            ivDown.setVisibility(View.GONE);
        }
        return this;
    }

    @OnClick(R.id.tv_nolike)
    public void onViewClicked() {
        if (nolikeClickListener != null) {
            nolikeClickListener.onNoLikeClick(position);
        }
    }

    public interface INolikeClickListener{
        void onNoLikeClick(int position);
    }

    public void setNolikeClickListener(INolikeClickListener nolikeClickListener) {
        this.nolikeClickListener = nolikeClickListener;
    }
}
