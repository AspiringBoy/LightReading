package com.dreamer_yy.lightreading.ui.news.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dreamer_yy.lightreading.R;
import com.dreamer_yy.lightreading.base.BaseFragment;
import com.dreamer_yy.lightreading.bean.NewsDetail;
import com.dreamer_yy.lightreading.component.ApplicationComponent;
import com.dreamer_yy.lightreading.component.DaggerHttpComponent;
import com.dreamer_yy.lightreading.ui.news.contract.DetailContract;
import com.dreamer_yy.lightreading.ui.news.presenter.DetailPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * Created by Dreamer__YY on 2018/5/21.
 */

public class DetailFragment extends BaseFragment<DetailPresenter> implements DetailContract.View {

    @BindView(R.id.rclv)
    RecyclerView rclv;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.toast_tv)
    TextView toastTv;
    @BindView(R.id.top_toast_rll)
    RelativeLayout topToastRll;
    Unbinder unbinder;

    public static DetailFragment newInstance(String newsid, int position) {
        Bundle args = new Bundle();
        args.putString("newsid", newsid);
        args.putInt("position", position);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_detail;
    }

    @Override
    public void initInjector(ApplicationComponent applicationComponent) {
        DaggerHttpComponent
                .builder()
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
    public void loadBannerData(NewsDetail newsDetail) {

    }

    @Override
    public void loadTopNewsData(NewsDetail newsDetail) {

    }

    @Override
    public void loadData(List<NewsDetail.ItemBean> itemBeanList) {

    }

    @Override
    public void loadMoreData(List<NewsDetail.ItemBean> itemBeanList) {

    }

}
