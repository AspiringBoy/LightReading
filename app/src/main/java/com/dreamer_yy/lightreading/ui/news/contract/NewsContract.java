package com.dreamer_yy.lightreading.ui.news.contract;

import com.dreamer_yy.lightreading.base.BaseContract;
import com.dreamer_yy.lightreading.bean.Channel;

import java.util.List;

/**
 * Created by Dreamer__YY on 2018/5/17.
 */

public interface NewsContract {
    interface View extends BaseContract.BaseView{
        void loadData(List<Channel> channels,List<Channel> otherChannels);
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void getChannel();
    }
}
