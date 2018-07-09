package com.dreamer_yy.lightreading.component;

import android.content.Context;

import com.dreamer_yy.lightreading.MyApp;
import com.dreamer_yy.lightreading.module.ApplicationModule;
import com.dreamer_yy.lightreading.module.HttpModule;
import com.dreamer_yy.lightreading.net.NewsApi;

import dagger.Component;

/**
 * Created by Dreamer__YY on 2018/4/25.
 */

@Component(modules = {ApplicationModule.class, HttpModule.class})
public interface ApplicationComponent {
    MyApp getApplication();

    Context getContext();

    NewsApi getNewsApi();
}
