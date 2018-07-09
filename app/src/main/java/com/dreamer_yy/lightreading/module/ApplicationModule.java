package com.dreamer_yy.lightreading.module;

import android.content.Context;

import com.dreamer_yy.lightreading.MyApp;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Dreamer__YY on 2018/4/25.
 */

@Module
public class ApplicationModule {
    private Context mContext;

    public ApplicationModule(Context mContext) {
        this.mContext = mContext;
    }

    @Provides
    public MyApp provideApplication(){
        return ((MyApp) mContext.getApplicationContext());
    }

    @Provides
    public Context provideContext(){
        return mContext;
    }
}
