package com.dreamer_yy.lightreading.net;

import android.support.annotation.StringDef;

import com.dreamer_yy.lightreading.bean.NewsArticleBean;
import com.dreamer_yy.lightreading.bean.NewsDetail;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Dreamer__YY on 2018/5/23.
 */

public class NewsApi {
    private static NewsApi mInstance;
    private NewsServiceApi mServiceApi;
    public static final String ACTION_DEFAULT = "default";
    public static final String ACTION_DOWN = "down";
    public static final String ACTION_UP = "up";

    @StringDef({ACTION_DEFAULT,ACTION_DOWN,ACTION_UP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Action{

    }

    private NewsApi(NewsServiceApi mServiceApi) {
        this.mServiceApi = mServiceApi;
    }

    /**
     * 单例获取该类实例
     * @param newsServiceApi
     * @return
     */
    public static NewsApi getInstance(NewsServiceApi newsServiceApi){
        if (mInstance == null) {
            mInstance = new NewsApi(newsServiceApi);
        }
        return mInstance;
    }

    public Observable<List<NewsDetail>> getNewsDetail(String id,@Action String action,int pullNum){
        return mServiceApi.getNewsDetail(id,action,pullNum);
    }

    public Observable<NewsArticleBean> getNewsArticle(String aid){
        if (aid.startsWith("sub")){
            return mServiceApi.getNewsArticleWithSub(aid);
        }else {
            return mServiceApi.getNewsArticleWithCmpp(ApiConstants.sGetNewsArticleCmppApi + ApiConstants.sGetNewsArticleDocCmppApi,aid);
        }
    }
}
