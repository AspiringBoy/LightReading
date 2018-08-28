package com.dreamer_yy.lightreading.ui.video.fragment;

import android.os.Bundle;
import android.view.View;

import com.dreamer_yy.lightreading.base.BaseFragment;
import com.dreamer_yy.lightreading.bean.VideoChannelBean;
import com.dreamer_yy.lightreading.bean.VideoDetailBean;
import com.dreamer_yy.lightreading.component.ApplicationComponent;
import com.dreamer_yy.lightreading.ui.video.contract.VideoContract;
import com.dreamer_yy.lightreading.ui.video.presenter.VideoPresenter;

import java.util.List;

/**
 * Created by Dreamer__YY on 2018/8/27.
 */

public class VideoDetailFragment extends BaseFragment<VideoPresenter> implements VideoContract.View{

    public static final String TYPEID = "typeId";

    public static VideoDetailFragment getInstance(String typeId){
        VideoDetailFragment fragment = new VideoDetailFragment();
        Bundle args = new Bundle();
        args.putCharSequence(TYPEID, typeId);
        return fragment;
    }

    @Override
    public void loadVideoChannel(List<VideoChannelBean> channelBean) {

    }

    @Override
    public void loadVideoDetails(List<VideoDetailBean> detailBean) {

    }

    @Override
    public void loadMoreVideoDetails(List<VideoDetailBean> detailBean) {

    }

    @Override
    public int getContentLayout() {
        return 0;
    }

    @Override
    public void initInjector(ApplicationComponent applicationComponent) {

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
}
