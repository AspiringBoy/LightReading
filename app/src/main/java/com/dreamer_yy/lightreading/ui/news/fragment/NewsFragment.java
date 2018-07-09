package com.dreamer_yy.lightreading.ui.news.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.dreamer_yy.lightreading.R;
import com.dreamer_yy.lightreading.base.BaseFragment;
import com.dreamer_yy.lightreading.bean.Channel;
import com.dreamer_yy.lightreading.component.ApplicationComponent;
import com.dreamer_yy.lightreading.component.DaggerHttpComponent;
import com.dreamer_yy.lightreading.ui.adapter.ChannelPagerAdapter;
import com.dreamer_yy.lightreading.ui.news.contract.NewsContract;
import com.dreamer_yy.lightreading.ui.news.presenter.NewsPresenter;
import com.dreamer_yy.lightreading.widget.CustomViewPager;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Dreamer__YY on 2018/5/18.
 */

public class NewsFragment extends BaseFragment<NewsPresenter> implements NewsContract.View {
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.SlidingTabLayout)
    com.flyco.tablayout.SlidingTabLayout mSlidingTabLayout;
    @BindView(R.id.iv_edit)
    ImageButton ivEdit;
    @BindView(R.id.viewpager)
    CustomViewPager viewpager;

    private List<Channel> mSelectDatas;
    private List<Channel> mUnSelectDatas;
    private String mSelectChannel;
    private int mSelectIndex;
    private ChannelPagerAdapter channelPagerAdapter;

    public static NewsFragment getInstance(){
        NewsFragment newsFragment = new NewsFragment();
        Bundle bundle = new Bundle();
        newsFragment.setArguments(bundle);
        return newsFragment;
    }

    @Override
    public void loadData(List<Channel> channels, List<Channel> otherChannels) {
        if (channels != null) {
            mSelectDatas.clear();
            mSelectDatas.addAll(channels);
            mUnSelectDatas.clear();
            mUnSelectDatas.addAll(otherChannels);
            channelPagerAdapter = new ChannelPagerAdapter(getChildFragmentManager(), channels);
            viewpager.setAdapter(channelPagerAdapter);
            viewpager.setOffscreenPageLimit(2);
            viewpager.setCurrentItem(0,false);
            mSlidingTabLayout.setViewPager(viewpager);
        }
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_news;
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
        mSelectDatas = new ArrayList<>();
        mUnSelectDatas = new ArrayList<>();
        mPresenter.getChannel();
    }

    @Override
    public void bindView(View view, Bundle saveInstanceState) {
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mSelectIndex = position;
                mSelectChannel = mSelectDatas.get(position).getChannelName();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onReTry() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.iv_edit)
    public void onViewClicked() {
    }
}
