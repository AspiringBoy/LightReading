package com.dreamer_yy.lightreading.ui.news.contract;

import com.dreamer_yy.lightreading.base.BaseContract;
import com.dreamer_yy.lightreading.bean.NewsArticleBean;

/**
 * Created by Dreamer__YY on 2018/8/11.
 */

public interface ArticleReadContract {
    interface View extends BaseContract.BaseView{
        void loadData(NewsArticleBean bean);
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void getData(String aid);
    }
}
