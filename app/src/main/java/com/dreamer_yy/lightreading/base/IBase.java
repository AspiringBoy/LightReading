package com.dreamer_yy.lightreading.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamer_yy.lightreading.component.ApplicationComponent;

/**
 * Created by Dreamer__YY on 2018/4/24.
 */

public interface IBase {
    View createView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState);

    View getView();

    int getContentLayout();

    void initInjector(ApplicationComponent applicationComponent);

    void initData();

    void bindView(View view, Bundle saveInstanceState);

}
