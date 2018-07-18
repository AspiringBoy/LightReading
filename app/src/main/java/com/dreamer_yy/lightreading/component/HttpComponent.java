package com.dreamer_yy.lightreading.component;

import com.dreamer_yy.lightreading.ui.jiandan.fragment.JianDanFragment;
import com.dreamer_yy.lightreading.ui.news.fragment.DetailFragment;
import com.dreamer_yy.lightreading.ui.news.fragment.NewsFragment;
import com.dreamer_yy.lightreading.ui.personal.PersonalFragment;
import com.dreamer_yy.lightreading.ui.video.fragment.VideoFragment;

import dagger.Component;

/**
 * Created by Dreamer__YY on 2018/5/18.
 */

@Component(dependencies = ApplicationComponent.class)
public interface HttpComponent {
    void inject(NewsFragment newsFragment);

    void inject(JianDanFragment jianDanFragment);

    void inject(VideoFragment videoFragment);

    void inject(PersonalFragment personalFragment);

    void inject(DetailFragment detailFragment);
}
