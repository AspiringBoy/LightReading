package com.dreamer_yy.lightreading.ui.video.presenter;

import com.dreamer_yy.lightreading.base.BasePresenter;
import com.dreamer_yy.lightreading.net.NewsApi;
import com.dreamer_yy.lightreading.ui.video.contract.VideoContract;

import javax.inject.Inject;

/**
 * Created by Dreamer__YY on 2018/8/27.
 */

public class VideoPresenter extends BasePresenter<VideoContract.View> implements VideoContract.Presenter {
    private NewsApi mNewsApi;

    @Inject
    public VideoPresenter(NewsApi mNewsApi) {
        this.mNewsApi = mNewsApi;
    }

    @Override
    public void getVideoChannel() {

    }

    @Override
    public void getVideoDetails(int page, String listType, String typeId) {

    }
}
