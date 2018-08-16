package com.dreamer_yy.lightreading.ui.news.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.dreamer_yy.lightreading.R;
import com.dreamer_yy.lightreading.base.BaseActivity;
import com.dreamer_yy.lightreading.bean.NewsArticleBean;
import com.dreamer_yy.lightreading.component.ApplicationComponent;
import com.dreamer_yy.lightreading.component.DaggerHttpComponent;
import com.dreamer_yy.lightreading.ui.news.contract.ArticleReadContract;
import com.dreamer_yy.lightreading.ui.news.presenter.ArticleReadPresenter;
import com.dreamer_yy.lightreading.utils.DateUtil;
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
    RelativeLayout mConstraintLayout;
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.state_view)
    com.dreamer_yy.lightreading.widget.SimpleMultiStateView SimpleMultiStateView;
    @BindView(R.id.ScrollView)
    ObservableScrollView observableScrollView;

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
        setStatusBarColor(Color.parseColor("#BDBDBD"),30);
        setWebSettings();
        observableScrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (y > mConstraintLayout.getHeight()) {
                    rlTop.setVisibility(View.VISIBLE);
                }else rlTop.setVisibility(View.GONE);
            }
        });
    }

    private void setWebSettings() {
        addJs();
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setAllowFileAccessFromFileURLs(true);
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.setVerticalScrollBarEnabled(false);
        webview.setVerticalScrollbarOverlay(false);
        webview.setHorizontalScrollBarEnabled(false);
        webview.setHorizontalScrollbarOverlay(false);
        webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webview.getSettings().setDomStorageEnabled(true);
        webview.loadUrl("file:///android_asset/ifeng/post_detail.html");
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String aid = getIntent().getStringExtra("aid");
                mPresenter.getData(aid);
            }
        });
    }

    private void addJs() {
        class JsObject {
            @JavascriptInterface
            public void jsFunctionimg(final String i) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("ArticleReadActivity", "run: " + i);
                    }
                });

            }

        }
        webview.addJavascriptInterface(new JsObject(), "jscontrolimg");
    }

    @Override
    public void bindView(View view, Bundle saveInstanceState) {

    }

    @Override
    public void onReTry() {
        String aid = getIntent().getStringExtra("aid");
        mPresenter.getData(aid);
    }

    @Override
    public void loadData(final NewsArticleBean articleBean) {
        if (articleBean == null) {
            showFaild();
            return;
        }

        tvTitle.setText(articleBean.getBody().getTitle());
        tvUpdateTime.setText(DateUtil.getTimestampString(DateUtil.string2Date(articleBean.getBody().getUpdateTime(), "yyyy/MM/dd HH:mm:ss")));
        if (articleBean.getBody().getSubscribe() != null) {
            Glide.with(this).load(articleBean.getBody().getSubscribe().getLogo())
                    .apply(new RequestOptions()
                            .transform(new CircleCrop())
                            //.placeholder()
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(ivLogo);
            Glide.with(this).load(articleBean.getBody().getSubscribe().getLogo())
                    .apply(new RequestOptions()
                            .transform(new CircleCrop())
                            //.placeholder()
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(ivTopLogo);
            tvTopname.setText(articleBean.getBody().getSubscribe().getCateSource());
            tvName.setText(articleBean.getBody().getSubscribe().getCateSource());
            tvTopUpdateTime.setText(articleBean.getBody().getSubscribe().getCatename());
        } else {
            tvTopname.setText(articleBean.getBody().getSource());
            tvName.setText(articleBean.getBody().getSource());
            tvTopUpdateTime.setText(!TextUtils.isEmpty(articleBean.getBody().getAuthor()) ? articleBean.getBody().getAuthor() : articleBean.getBody().getEditorcode());
        }
        webview.post(new Runnable() {
            @Override
            public void run() {
                final String content = articleBean.getBody().getText();
                String url = "javascript:show_content(\'" + content + "\')";
                webview.loadUrl(url);
                showSuccess();
            }
        });
    }
}
