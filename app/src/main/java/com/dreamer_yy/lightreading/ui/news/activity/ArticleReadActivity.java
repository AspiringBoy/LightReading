package com.dreamer_yy.lightreading.ui.news.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dreamer_yy.lightreading.R;
import com.dreamer_yy.lightreading.base.BaseActivity;
import com.dreamer_yy.lightreading.bean.NewsArticleBean;
import com.dreamer_yy.lightreading.component.ApplicationComponent;
import com.dreamer_yy.lightreading.component.DaggerHttpComponent;
import com.dreamer_yy.lightreading.ui.news.contract.ArticleReadContract;
import com.dreamer_yy.lightreading.ui.news.presenter.ArticleReadPresenter;
import com.dreamer_yy.lightreading.widget.ObservableScrollView;
import com.dreamer_yy.lightreading.widget.SimpleMultiStateView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ArticleReadActivity extends BaseActivity<ArticleReadPresenter> implements ArticleReadContract.View {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_topLogo)
    ImageView ivTopLogo;
    @BindView(R.id.tv_topname)
    TextView tvTopname;
    @BindView(R.id.tv_TopUpdateTime)
    TextView tvTopUpdateTime;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_updateTime)
    TextView tvUpdateTime;
    @BindView(R.id.bt_like)
    Button btLike;
    @BindView(R.id.ConstraintLayout)
    RelativeLayout ConstraintLayout;
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.state_view)
    com.dreamer_yy.lightreading.widget.SimpleMultiStateView SimpleMultiStateView;
    @BindView(R.id.ScrollView)
    ObservableScrollView ScrollView;

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_article_read;
    }

    @Override
    public void initInjector(ApplicationComponent applicationComponent) {
        DaggerHttpComponent
                .builder()
                .applicationComponent(applicationComponent)
                .build()
                .inject(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void bindView(View view, Bundle saveInstanceState) {

    }

    @Override
    public void onReTry() {

    }

    @Override
    public void loadData(NewsArticleBean bean) {
        if (bean == null) {
            showFaild();
            return;
        }

    }
}
