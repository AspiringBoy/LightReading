package com.dreamer_yy.lightreading.ui.news.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.dreamer_yy.lightreading.MyApp;
import com.dreamer_yy.lightreading.R;
import com.dreamer_yy.lightreading.base.BaseFragment;
import com.dreamer_yy.lightreading.bean.NewsDetail;
import com.dreamer_yy.lightreading.component.ApplicationComponent;
import com.dreamer_yy.lightreading.component.DaggerHttpComponent;
import com.dreamer_yy.lightreading.net.NewsApi;
import com.dreamer_yy.lightreading.net.NewsUtils;
import com.dreamer_yy.lightreading.ui.adapter.NewsDetailAdapter;
import com.dreamer_yy.lightreading.ui.news.activity.ArticleReadActivity;
import com.dreamer_yy.lightreading.ui.news.activity.ImageBrowseActivity;
import com.dreamer_yy.lightreading.ui.news.contract.DetailContract;
import com.dreamer_yy.lightreading.ui.news.presenter.DetailPresenter;
import com.dreamer_yy.lightreading.utils.ContextUtils;
import com.dreamer_yy.lightreading.utils.ImageLoaderUtils;
import com.dreamer_yy.lightreading.widget.CustomLoadMoreView;
import com.dreamer_yy.lightreading.widget.NewsDelPop;
import com.flyco.animation.SlideEnter.SlideRightEnter;
import com.flyco.animation.SlideExit.SlideRightExit;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

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
    private NewsDetailAdapter detailAdapter;
    private ArrayList<NewsDetail.ItemBean> beanList;
    private ArrayList<NewsDetail.ItemBean> bannerList;
    private String newsid;
    private int position;
    private int upPullNum = 1;
    private int downPullNum = 1;
    private boolean isRemoveHeaderView = false;
    private View rclvHeaderView;
    private Banner headerBanner;
    private NewsDelPop newsDelPop;

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
        if (getArguments() == null) return;
        newsid = getArguments().getString("newsid");
        position = getArguments().getInt("position");
        mPresenter.getData(newsid, NewsApi.ACTION_DEFAULT, 1);
    }

    @Override
    public void bindView(View view, Bundle saveInstanceState) {
        ptrLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame,rclv,header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                isRemoveHeaderView = true;
                mPresenter.getData(newsid, NewsApi.ACTION_DOWN, downPullNum);
            }
        });
        ptrLayout.disableWhenHorizontalMove(true);
        rclv.setLayoutManager(new LinearLayoutManager(getActivity()));
        beanList = new ArrayList<>();
        bannerList = new ArrayList<>();
        detailAdapter = new NewsDetailAdapter(beanList, getActivity());
        rclv.setAdapter(detailAdapter);
        detailAdapter.setEnableLoadMore(true);
        detailAdapter.setLoadMoreView(new CustomLoadMoreView());
        detailAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        detailAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getData(newsid, NewsApi.ACTION_UP, upPullNum);
            }
        },rclv);
        rclv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                NewsDetail.ItemBean itemBean = (NewsDetail.ItemBean) adapter.getItem(position);
                toRead(itemBean);
            }
        });
        rclv.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                NewsDetail.ItemBean itemBean = (NewsDetail.ItemBean) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.iv_close:
                        view.getHeight();
                        int[] location = new int[2];
                        view.getLocationInWindow(location);
                        Log.i("DetailFragment", "点击的item的高度:" + view.getHeight() + "x值:" + location[0] + "y值" + location[1]);
                        if (itemBean.getStyle() == null) return;
                        if (ContextUtils.getSreenWidth(MyApp.getContext()) - 50 - location[1] < ContextUtils.dip2px(MyApp.getContext(), 80)) {
                            newsDelPop
                                    .anchorView(view)
                                    .gravity(Gravity.TOP)
                                    .setBackReason(itemBean.getStyle().getBackreason(), true, position)
                                    .show();
                        } else {
                            newsDelPop
                                    .anchorView(view)
                                    .gravity(Gravity.BOTTOM)
                                    .setBackReason(itemBean.getStyle().getBackreason(), false, position)
                                    .show();
                        }
                        break;
                }
            }
        });

        rclvHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.news_detail_headerview, null);
        headerBanner = ((Banner) rclvHeaderView.findViewById(R.id.banner));
        headerBanner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
                .setDelayTime(3000)
                .setIndicatorGravity(BannerConfig.RIGHT)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        ImageLoaderUtils.loadImage(context,path,imageView);
                    }
                })
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        if (bannerList.size() < 1) return;
                        bannerToRead(bannerList.get(position));
                    }
                });

        newsDelPop = new NewsDelPop(getActivity());
        newsDelPop.alignCenter(false)
                .widthScale(0.95f)
                .showAnim(new SlideRightEnter())
                .dismissAnim(new SlideRightExit())
                .offset(-100,0)
                .dimEnabled(true)
                .setNolikeClickListener(new NewsDelPop.INolikeClickListener() {
                    @Override
                    public void onNoLikeClick(int position) {
                        newsDelPop.dismiss();
                        detailAdapter.remove(position);
                        showToast(0,false);
                    }
                });
    }

    private void toRead(NewsDetail.ItemBean itemBean) {
        if (itemBean == null) {
            return;
        }
        switch (itemBean.getItemType()) {
            case NewsDetail.ItemBean.TYPE_DOC_TITLEIMG:
            case NewsDetail.ItemBean.TYPE_DOC_SLIDEIMG:
                Intent intent = new Intent(getActivity(), ArticleReadActivity.class);
                intent.putExtra("aid", itemBean.getDocumentId());
                startActivity(intent);
                break;
            case NewsDetail.ItemBean.TYPE_SLIDE:
                ImageBrowseActivity.launch(getActivity(), itemBean);
                break;
            case NewsDetail.ItemBean.TYPE_ADVERT_TITLEIMG:
            case NewsDetail.ItemBean.TYPE_ADVERT_SLIDEIMG:
            case NewsDetail.ItemBean.TYPE_ADVERT_LONGIMG:
//                AdvertActivity.launch(getActivity(), itemBean.getLink().getWeburl());
                break;
            case NewsDetail.ItemBean.TYPE_PHVIDEO:

                break;
        }
    }

    private void bannerToRead(NewsDetail.ItemBean itemBean) {
        if (itemBean == null) {
            return;
        }
        switch (itemBean.getType()) {
            case NewsUtils.TYPE_DOC:
                Intent intent = new Intent(getActivity(), ArticleReadActivity.class);
                intent.putExtra("aid", itemBean.getDocumentId());
                startActivity(intent);
                break;
            case NewsUtils.TYPE_SLIDE:
                ImageBrowseActivity.launch(getActivity(), itemBean);
                break;
            case NewsUtils.TYPE_ADVERT:
//                AdvertActivity.launch(getActivity(), itemBean.getLink().getWeburl());
                break;
            case NewsUtils.TYPE_PHVIDEO:
//                T("TYPE_PHVIDEO");
                break;
        }
    }

    private void showToast(int num, boolean isRefresh) {
        if (isRefresh) {
            toastTv.setText(String.format(getResources().getString(R.string.news_toast), num + ""));
        } else {
            toastTv.setText("将为你减少此类内容");
        }
        topToastRll.setVisibility(View.VISIBLE);
        ViewAnimator.animate(topToastRll)
                .newsPaper()
                .duration(1000)
                .start()
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        ViewAnimator.animate(topToastRll)
                                .bounceOut()
                                .duration(1000)
                                .start();
                    }
                });
    }

    @Override
    public void onReTry() {

    }

    @Override
    public void loadBannerData(NewsDetail newsDetail) {
        bannerList.clear();
        ArrayList<String> urlList = new ArrayList<>();
        ArrayList<String> titleList = new ArrayList<>();
        for (NewsDetail.ItemBean itemBean : newsDetail.getItem()) {
            if (!TextUtils.isEmpty(itemBean.getThumbnail())) {
                bannerList.add(itemBean);
                urlList.add(itemBean.getThumbnail());
                titleList.add(itemBean.getTitle());
            }
        }
        if (urlList.size() > 0) {
            headerBanner.setImages(urlList);
            headerBanner.setBannerTitles(titleList);
            headerBanner.start();
            if (detailAdapter.getHeaderLayoutCount() < 1) {
                detailAdapter.addHeaderView(rclvHeaderView);
            }
        }
    }

    @Override
    public void loadTopNewsData(NewsDetail newsDetail) {
        Log.d("Dreamer__YY:", "loadTopNewsData: ");
    }

    @Override
    public void loadData(List<NewsDetail.ItemBean> itemBeanList) {
        if (itemBeanList == null || itemBeanList.size() == 0) {
            ptrLayout.refreshComplete();
            showFaild();
        }else {
            downPullNum++;
            if (isRemoveHeaderView) {
                detailAdapter.removeAllHeaderView();
            }
            detailAdapter.setNewData(itemBeanList);
            ptrLayout.refreshComplete();
            showToast(itemBeanList.size(),true);
            showSuccess();
        }
    }

    @Override
    public void loadMoreData(List<NewsDetail.ItemBean> itemBeanList) {
        if (itemBeanList == null || itemBeanList.size() == 0) {
            detailAdapter.loadMoreFail();
        } else {
            upPullNum++;
            detailAdapter.addData(itemBeanList);
            detailAdapter.loadMoreComplete();
        }
    }

}
