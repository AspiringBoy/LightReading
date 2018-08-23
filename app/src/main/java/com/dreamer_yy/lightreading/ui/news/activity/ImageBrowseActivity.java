package com.dreamer_yy.lightreading.ui.news.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dreamer_yy.lightreading.R;
import com.dreamer_yy.lightreading.base.BaseActivity;
import com.dreamer_yy.lightreading.bean.NewsArticleBean;
import com.dreamer_yy.lightreading.bean.NewsDetail;
import com.dreamer_yy.lightreading.bean.NewsImagesBean;
import com.dreamer_yy.lightreading.component.ApplicationComponent;
import com.dreamer_yy.lightreading.component.DaggerHttpComponent;
import com.dreamer_yy.lightreading.net.ApiConstants;
import com.dreamer_yy.lightreading.ui.news.contract.ArticleReadContract;
import com.dreamer_yy.lightreading.ui.news.presenter.ArticleReadPresenter;
import com.dreamer_yy.lightreading.utils.ImageLoaderUtils;
import com.dreamer_yy.lightreading.utils.StatusBarUtil;
import com.dreamer_yy.lightreading.widget.DragBackLayout;
import com.dreamer_yy.lightreading.widget.HackyViewPager;
import com.dreamer_yy.lightreading.widget.MyScrollView;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageBrowseActivity extends BaseActivity<ArticleReadPresenter> implements ArticleReadContract.View {

    private static final String AID = "aid";
    private static final String ISCMPP = "isCmpp";
    @BindView(R.id.image_vp)
    HackyViewPager imageVp;
    @BindView(R.id.drag_back_view)
    DragBackLayout dragBackView;
    @BindView(R.id.titlebar_name)
    TextView titlebarName;
    @BindView(R.id.rll_top)
    RelativeLayout rllTop;
    @BindView(R.id.img_desc_tv)
    TextView imgDescTv;
    @BindView(R.id.myscrollview)
    MyScrollView myscrollview;
    @BindView(R.id.relativelayout)
    RelativeLayout mRelativelayout;
    private boolean isShow = true;
    private NewsArticleBean newsArticleBean;

    public static void launch(Activity context, NewsDetail.ItemBean bodyBean) {
        Intent intent = new Intent(context, ImageBrowseActivity.class);
        if (bodyBean.getId().contains(ApiConstants.sGetNewsArticleCmppApi)
                || bodyBean.getDocumentId().startsWith("cmpp")) {
            intent.putExtra(ISCMPP, true);
        } else {
            intent.putExtra(ISCMPP, false);
        }
        intent.putExtra(AID, bodyBean.getDocumentId());
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void loadData(NewsArticleBean bean) {
        try {
            this.newsArticleBean = bean;
            imgDescTv.setText("1 / " + newsArticleBean.getBody().getSlides().size() + " " + newsArticleBean.getBody().getSlides().get(0).getDescription());
            imageVp.setAdapter(new ViewPagerAdapter(newsArticleBean.getBody().getSlides()));
            titlebarName.setText(newsArticleBean.getBody().getTitle());
            //showSuccess();
        } catch (Exception e) {
            // showFaild();
            e.printStackTrace();
        }
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_image_browse;
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
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, android.R.color.black));
        mSwipeBackHelper.setSwipeBackEnable(true);
        mRelativelayout.getBackground().setAlpha(255);
        dragBackView.setDragDirectMode(DragBackLayout.DragDirectMode.VERCITAL);
        dragBackView.setDragBackListener(new DragBackLayout.DragBackListener() {
            @Override
            public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {
                mRelativelayout.getBackground().setAlpha(255 - (int) Math.ceil(255 * fractionAnchor));
                DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
                df.setMaximumFractionDigits(1);
                df.setRoundingMode(RoundingMode.HALF_UP);
                String dd = df.format(fractionAnchor);
                double alpha = 1 - (Float.valueOf(dd) + 0.8);
                if (fractionAnchor == 0 && isShow) {
                    myscrollview.setAlpha(1f);
                    rllTop.setAlpha(1f);
                    rllTop.setVisibility(View.VISIBLE);
                    myscrollview.setVisibility(View.VISIBLE);
                } else {
                    if (alpha == 0) {
                        rllTop.setVisibility(View.GONE);
                        myscrollview.setVisibility(View.GONE);
                        myscrollview.setAlpha(1f);
                        rllTop.setAlpha(1f);
                    } else {
                        if (rllTop.getVisibility() != View.GONE) {
                            rllTop.setAlpha((float) alpha);
                            myscrollview.setAlpha((float) alpha);
                        }
                    }
                }
            }
        });
        rllTop.getBackground().mutate().setAlpha(100);
        myscrollview.getBackground().mutate().setAlpha(100);
        imageVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                imgDescTv.setText((position + 1) + " / " + newsArticleBean.getBody().getSlides().size() + " " + newsArticleBean.getBody().getSlides().get(position).getDescription());
                if (position == 0) {
                    mSwipeBackHelper.setSwipeBackEnable(true);
                } else mSwipeBackHelper.setSwipeBackEnable(false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void bindView(View view, Bundle saveInstanceState) {
        if (getIntent().getExtras() == null) return;
        String aid = getIntent().getStringExtra(AID);
        boolean isCmpp = getIntent().getBooleanExtra(ISCMPP, false);
        mPresenter.getData(aid);
    }

    @Override
    public void onReTry() {
        bindView(null, null);
    }

    @OnClick(R.id.titlebar_left)
    public void onViewClicked() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private class ViewPagerAdapter extends PagerAdapter {
        private List<NewsArticleBean.BodyBean.SlidesBean> slidesBeans;
        private PhotoView photoView;
        private ProgressBar progressBar;

        public ViewPagerAdapter(List<NewsArticleBean.BodyBean.SlidesBean> slidesBeans) {
            this.slidesBeans = slidesBeans;
        }

        @Override
        public int getCount() {
            return slidesBeans == null ? 0 : slidesBeans.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(ImageBrowseActivity.this).inflate(R.layout.loadimage, container, false);
            photoView = ((PhotoView) view.findViewById(R.id.photoview));
            progressBar = ((ProgressBar) view.findViewById(R.id.loading));
            photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(ImageView view, float x, float y) {
                    if (isShow) {
                        setView(rllTop,false);
                        setView(myscrollview,false);
                        isShow = false;
                    }else {
                        setView(rllTop,true);
                        setView(myscrollview,true);
                        isShow = true;
                    }
                }
            });
            progressBar.setVisibility(View.GONE);
            ImageLoaderUtils.loadImage(ImageBrowseActivity.this, slidesBeans.get(position).getImage(), new DrawableImageViewTarget(photoView){
                @Override
                public void setDrawable(Drawable drawable) {
                    super.setDrawable(drawable);
                }

                @Override
                public void onLoadStarted(@Nullable Drawable placeholder) {
                    super.onLoadStarted(placeholder);
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    super.onLoadFailed(errorDrawable);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                    super.onLoadCleared(placeholder);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    super.onResourceReady(resource, transition);
                    progressBar.setVisibility(View.GONE);
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        private void setView(final View view, final boolean isShow) {
            AlphaAnimation alphaAnimation;
            if (isShow) {
                alphaAnimation = new AlphaAnimation(0, 1);
            } else {
                alphaAnimation = new AlphaAnimation(1, 0);
            }
            alphaAnimation.setFillAfter(true);
            alphaAnimation.setDuration(500);
            view.startAnimation(alphaAnimation);
            alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(isShow ? View.VISIBLE : View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }
}
