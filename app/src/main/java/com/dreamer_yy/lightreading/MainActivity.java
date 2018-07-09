package com.dreamer_yy.lightreading;

import android.os.Bundle;
import android.view.View;

import com.dreamer_yy.lightreading.base.BaseActivity;
import com.dreamer_yy.lightreading.base.SupportFragment;
import com.dreamer_yy.lightreading.component.ApplicationComponent;
import com.dreamer_yy.lightreading.ui.jiandan.fragment.JianDanFragment;
import com.dreamer_yy.lightreading.ui.news.fragment.NewsFragment;
import com.dreamer_yy.lightreading.ui.personal.PersonalFragment;
import com.dreamer_yy.lightreading.ui.video.fragment.VideoFragment;
import com.dreamer_yy.lightreading.utils.StatusBarUtil;
import com.dreamer_yy.lightreading.widget.BottomBarTab;
import com.dreamer_yy.lightreading.widget.BottomBarView;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.bottom_bar)
    BottomBarView bottomBarView;
    SupportFragment[] fragments = new SupportFragment[4];

    @Override
    public int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initInjector(ApplicationComponent applicationComponent) {

    }

    @Override
    public void initData() {

    }

    @Override
    public void bindView(View view, Bundle saveInstanceState) {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        if (saveInstanceState == null) {
            fragments[0] = NewsFragment.getInstance();
            fragments[1] = VideoFragment.getInstance();
            fragments[2] = JianDanFragment.getInstance();
            fragments[3] = PersonalFragment.getInstance();
            getSupportDelegate().loadMultipleRootFragment(R.id.content_container, 0, fragments[0], fragments[1], fragments[2], fragments[3]);
        } else {
            fragments[0] = findFragment(NewsFragment.class);
            fragments[1] = findFragment(VideoFragment.class);
            fragments[2] = findFragment(JianDanFragment.class);
            fragments[3] = findFragment(PersonalFragment.class);
        }
        bottomBarView.addTabItem(new BottomBarTab(this, R.drawable.ic_news, "新闻"))
                .addTabItem(new BottomBarTab(this, R.drawable.ic_video, "视频"))
                .addTabItem(new BottomBarTab(this, R.drawable.ic_jiandan, "煎蛋"))
                .addTabItem(new BottomBarTab(this, R.drawable.ic_my, "我的"));
        bottomBarView.setOnTabSelectListener(new BottomBarView.ITabSelectListener() {
            @Override
            public void onTabSelect(int pos, int prePos) {
                getSupportDelegate().showHideFragment(fragments[pos],fragments[prePos]);
            }

            @Override
            public void onTabUnselect(int pos) {

            }

            @Override
            public void onTabReselect(int pos) {

            }
        });
    }

    @Override
    public void onReTry() {

    }

    /**
     * 是否支持滑动返回
     *
     * @return
     */
    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }
}
