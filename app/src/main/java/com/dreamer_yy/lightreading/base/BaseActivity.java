package com.dreamer_yy.lightreading.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dreamer_yy.lightreading.MyApp;
import com.dreamer_yy.lightreading.R;
import com.dreamer_yy.lightreading.widget.BaseMultiStateView;
import com.dreamer_yy.lightreading.widget.SimpleMultiStateView;
import com.trello.rxlifecycle2.LifecycleTransformer;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * Created by Dreamer__YY on 2018/4/24.
 */

public abstract class BaseActivity<T1 extends BaseContract.BasePresenter> extends SupportActivity implements IBase,BaseContract.BaseView,BGASwipeBackHelper.Delegate {

    @Nullable
    @Inject
    protected T1 mPresenter;
    @Nullable
    @BindView(R.id.state_view)
    SimpleMultiStateView mSimpleMultiStateView;
    protected BGASwipeBackHelper mSwipeBackHelper;
    private View mRootView;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //必须在oncreate之前调用
        initSwipeBackFinish();
        super.onCreate(savedInstanceState);
        mRootView = createView(null,null,savedInstanceState);
        setContentView(mRootView);
        initInjector(MyApp.getInstance().getApplicationComponent());
        attachView();
        initStateView();
        initData();
        bindView(mRootView,savedInstanceState);
    }

    private void initStateView() {
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.setEmptyLayout(R.layout.view_empty)
                    .setFaildLayout(R.layout.view_retry)
                    .setLoadingLayout(R.layout.view_loading)
                    .setNonetLayout(R.layout.view_nonet)
                    .build()
                    .setOnReloadListener(new BaseMultiStateView.OnReloadListener() {
                        @Override
                        public void onReload() {
                            onReTry();
                        }
                    });
        }
    }

    private void attachView() {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);
        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackManager.getInstance().init(this) 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。
        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(false);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f);
    }

    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = getLayoutInflater().inflate(getContentLayout(), container);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public View getView() {
        return mRootView;
    }

    @Override
    public void showEmpty() {
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.showEmptyView();
        }
    }

    @Override
    public void showFaild() {
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.showErrorView();
        }
    }

    @Override
    public void showLoading() {
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.showLoadingView();
        }
    }

    @Override
    public void showSuccess() {
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.showContent();
        }
    }

    @Override
    public void noNetWork() {
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.showNoNetView();
        }
    }

    protected SimpleMultiStateView getStateView() {
        return mSimpleMultiStateView;
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {

    }

    @Override
    public void onSwipeBackLayoutCancel() {

    }

    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }

}
