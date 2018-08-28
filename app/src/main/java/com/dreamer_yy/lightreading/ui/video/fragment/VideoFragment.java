package com.dreamer_yy.lightreading.ui.video.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamer_yy.lightreading.R;
import com.dreamer_yy.lightreading.base.BaseFragment;
import com.dreamer_yy.lightreading.bean.Channel;
import com.dreamer_yy.lightreading.bean.VideoChannelBean;
import com.dreamer_yy.lightreading.bean.VideoDetailBean;
import com.dreamer_yy.lightreading.component.ApplicationComponent;
import com.dreamer_yy.lightreading.component.DaggerHttpComponent;
import com.dreamer_yy.lightreading.ui.adapter.VideoPagerAdapter;
import com.dreamer_yy.lightreading.ui.news.contract.NewsContract;
import com.dreamer_yy.lightreading.ui.news.presenter.NewsPresenter;
import com.dreamer_yy.lightreading.ui.video.contract.VideoContract;
import com.dreamer_yy.lightreading.ui.video.presenter.VideoPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Dreamer__YY on 2018/5/18.
 */

public class VideoFragment extends BaseFragment<VideoPresenter> implements VideoContract.View {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.video_vp)
    ViewPager videoVp;
    private VideoPagerAdapter videoPagerAdapter;

    public static VideoFragment getInstance() {
        VideoFragment newsFragment = new VideoFragment();
        Bundle bundle = new Bundle();
        newsFragment.setArguments(bundle);
        return newsFragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_video;
    }

    @Override
    public void initInjector(ApplicationComponent applicationComponent) {
        DaggerHttpComponent.builder()
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
    public void loadVideoChannel(List<VideoChannelBean> channelBean) {
        videoPagerAdapter = new VideoPagerAdapter(getChildFragmentManager(), channelBean.get(0));
        videoVp.setAdapter(videoPagerAdapter);
        videoVp.setOffscreenPageLimit(1);
        videoVp.setCurrentItem(0,false);
        tabLayout.setupWithViewPager(videoVp,true);
    }

    @Override
    public void loadVideoDetails(List<VideoDetailBean> detailBean) {

    }

    @Override
    public void loadMoreVideoDetails(List<VideoDetailBean> detailBean) {

    }
}
