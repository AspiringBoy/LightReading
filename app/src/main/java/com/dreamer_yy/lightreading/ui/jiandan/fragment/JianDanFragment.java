package com.dreamer_yy.lightreading.ui.jiandan.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.dreamer_yy.lightreading.R;
import com.dreamer_yy.lightreading.base.BaseFragment;
import com.dreamer_yy.lightreading.bean.Channel;
import com.dreamer_yy.lightreading.component.ApplicationComponent;
import com.dreamer_yy.lightreading.component.DaggerHttpComponent;
import com.dreamer_yy.lightreading.ui.news.contract.NewsContract;
import com.dreamer_yy.lightreading.ui.news.presenter.NewsPresenter;
import com.dreamer_yy.lightreading.widget.CustomViewPager;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Dreamer__YY on 2018/5/18.
 */

public class JianDanFragment extends BaseFragment<NewsPresenter> implements NewsContract.View {
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.SlidingTabLayout)
    com.flyco.tablayout.SlidingTabLayout SlidingTabLayout;
    @BindView(R.id.iv_edit)
    ImageButton ivEdit;
    @BindView(R.id.viewpager)
    CustomViewPager viewpager;

    public static JianDanFragment getInstance(){
        JianDanFragment newsFragment = new JianDanFragment();
        Bundle bundle = new Bundle();
        newsFragment.setArguments(bundle);
        return newsFragment;
    }

    @Override
    public void loadData(List<Channel> channels, List<Channel> otherChannels) {

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

    }

    @Override
    public void bindView(View view, Bundle saveInstanceState) {

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
