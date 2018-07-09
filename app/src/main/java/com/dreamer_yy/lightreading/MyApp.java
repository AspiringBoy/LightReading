package com.dreamer_yy.lightreading;

import com.dreamer_yy.lightreading.component.ApplicationComponent;
import com.dreamer_yy.lightreading.component.DaggerApplicationComponent;
import com.dreamer_yy.lightreading.module.ApplicationModule;
import com.dreamer_yy.lightreading.module.HttpModule;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackManager;

/**
 * Created by Dreamer__YY on 2018/4/25.
 */

public class MyApp extends LitePalApplication{

    private ApplicationComponent applicationComponent;
    private static MyApp myApp;

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
        //初始化注射器(依赖注入)
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        //初始化litepal数据库
        LitePal.initialize(this);
        //初始化滑动返回
        BGASwipeBackManager.getInstance().init(this);
    }

    public static MyApp getInstance(){
        return myApp;
    }

    public ApplicationComponent getApplicationComponent(){
        return applicationComponent;
    }
}
