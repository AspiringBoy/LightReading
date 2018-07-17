package com.dreamer_yy.lightreading.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dreamer_yy.lightreading.MyApp;
import com.dreamer_yy.lightreading.R;
import com.dreamer_yy.lightreading.utils.DialogHelper;
import com.dreamer_yy.lightreading.widget.BaseMultiStateView;
import com.dreamer_yy.lightreading.widget.SimpleMultiStateView;
import com.trello.rxlifecycle2.LifecycleTransformer;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Dreamer__YY on 2018/5/8.
 */

public abstract class BaseFragment<T1 extends BaseContract.BasePresenter> extends SupportFragment implements IBase,BaseContract.BaseView {

    protected Context mContext;
    protected View mRootView;
    protected Dialog mLoadingDialog;
    @Nullable
    @Inject
    protected T1 mPresenter;
    @Nullable
    @BindView(R.id.state_view)
    SimpleMultiStateView mSimpleMultiStateView;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (mRootView != null) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
        }else {
            mRootView = createView(inflater,container,savedInstanceState);
        }
        mContext = mRootView.getContext();
        mLoadingDialog = DialogHelper.getLoadingDialog(mContext);
        return mRootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initData();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initInjector(MyApp.getInstance().getApplicationComponent());
        attachView();
        initStateView();
        bindView(view,savedInstanceState);
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

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(getContentLayout(), container,false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Nullable
    @Override
    public View getView() {
        return mRootView;
    }

    @Override
    public void showSuccess() {
        hideLoadingDialog();
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.showContent();
        }
    }

    @Override
    public void showLoading() {
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.showLoadingView();
        }
    }

    @Override
    public void showFaild() {
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.showErrorView();
        }
    }

    @Override
    public void showEmpty() {
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.showEmptyView();
        }
    }

    @Override
    public void noNetWork() {
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.showNoNetView();
        }
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }

    protected void showLoadingDialog(){
        if (mLoadingDialog != null) {
            mLoadingDialog.show();
        }
    }

    protected void showLoadingDialog(String dialogTxt){
        if (mLoadingDialog != null) {
            ((TextView) mLoadingDialog.findViewById(R.id.tv_load_dialog)).setText(dialogTxt);
            mLoadingDialog.show();
        }
    }
    protected void hideLoadingDialog(){
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }
}
