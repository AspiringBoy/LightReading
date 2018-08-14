package com.dreamer_yy.lightreading.ui.news.presenter;

import com.dreamer_yy.lightreading.base.BasePresenter;
import com.dreamer_yy.lightreading.bean.NewsArticleBean;
import com.dreamer_yy.lightreading.net.BaseObserver;
import com.dreamer_yy.lightreading.net.NewsApi;
import com.dreamer_yy.lightreading.net.RxSchedulers;
import com.dreamer_yy.lightreading.ui.news.contract.ArticleReadContract;

import javax.inject.Inject;

/**
 * Created by Dreamer__YY on 2018/8/14.
 */

public class ArticleReadPresenter extends BasePresenter<ArticleReadContract.View> implements ArticleReadContract.Presenter{

    private NewsApi newsApi;

    @Inject
    public ArticleReadPresenter(NewsApi newsApi) {
        this.newsApi = newsApi;
    }

    @Override
    public void getData(String aid) {
        newsApi.getNewsArticle(aid)
                .compose(RxSchedulers.<NewsArticleBean>applySchedulers())
                .compose(mView.<NewsArticleBean>bindToLife())
                .subscribe(new BaseObserver<NewsArticleBean>() {
                    @Override
                    public void onSuccess(NewsArticleBean bean) {
                        mView.loadData(bean);
                    }

                    @Override
                    public void onFaild(Throwable e) {
                        mView.loadData(null);
                    }
                });
    }
}
