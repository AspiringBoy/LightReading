package com.dreamer_yy.lightreading.module;

import com.dreamer_yy.lightreading.MyApp;
import com.dreamer_yy.lightreading.net.ApiConstants;
import com.dreamer_yy.lightreading.net.NewsApi;
import com.dreamer_yy.lightreading.net.NewsServiceApi;
import com.dreamer_yy.lightreading.net.RetrofitConfig;

import java.io.File;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dreamer__YY on 2018/4/26.
 */

@Module
public class HttpModule {

    @Provides
    public OkHttpClient.Builder provideOkhttpBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        Cache httpCache = new Cache(new File(MyApp.getContext().getCacheDir(), "httpCache"), 100 * 1024 * 1024);
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.connectTimeout(5, TimeUnit.SECONDS)
                .cache(httpCache)
                .retryOnConnectionFailure(true)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(RetrofitConfig.sLoggingInterceptor)
                .addInterceptor(RetrofitConfig.sRewriteCacheControlInterceptor)
                .addNetworkInterceptor(RetrofitConfig.sRewriteCacheControlInterceptor);
        return builder;
    }

    @Provides
    public NewsApi provideNewsApi(OkHttpClient.Builder builder) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ApiConstants.sIFengApi)
                .client(builder.build())
                .build();
        return NewsApi.getInstance(retrofit.create(NewsServiceApi.class));
    }


}
