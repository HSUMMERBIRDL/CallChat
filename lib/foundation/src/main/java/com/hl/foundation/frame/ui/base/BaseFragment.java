package com.hl.foundation.frame.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hl.foundation.frame.ui.BaseLoadingView;
import com.hl.foundation.frame.ui.BaseModel;
import com.hl.foundation.frame.ui.BasePresenter;
import com.hl.foundation.library.utils.TUtil;
import com.hl.foundation.library.widget.LoadingDialog;
import com.hl.foundation.library.widget.ToastUitl;

import butterknife.ButterKnife;

/**
 * des:
 * Created by HL
 * on 2017-05-03.
 */

public abstract class BaseFragment<M extends BaseModel,P extends BasePresenter> extends Fragment implements BaseLoadingView{

    protected View rootView;
    protected Context mContext;

    protected M mModel;
    protected P mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            getFromArguments(arguments);
        }
    }

    protected void getFromArguments(Bundle arguments) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if(null == rootView){
            rootView = inflater.inflate(getLayoutId(),container,false);
        }
        ButterKnife.bind(this,rootView);

        mModel =  TUtil.getT(this, 0);
        mPresenter =  TUtil.getT(this, 1);

        if(null != mPresenter){
            initPresenter();
        }

        initDatas();
        initViews();


        return rootView;


    }

    protected abstract int getLayoutId();

    protected void initDatas() {

    }

    protected abstract void initViews();

    private void initPresenter() {
        mPresenter.mContext = this.getActivity();
        mPresenter.setMV(mModel,this);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(null != mPresenter){
            mPresenter.onDestory();
        }
        ButterKnife.unbind(this);
    }

    @Override
    public void showLoading() {

        LoadingDialog.showDialogForLoading(getActivity(),"",true);
    }

    @Override
    public void showLoading(String msg) {
        LoadingDialog.showDialogForLoading(getActivity(),msg,true);
    }

    @Override
    public void showNotCancelLoading(String msg) {
        LoadingDialog.showDialogForLoading(getActivity(),msg,false);
    }

    @Override
    public void dismissLoading() {
        LoadingDialog.cancelDialogForLoading();
    }

    @Override
    public void showLongToast(String msg) {

        ToastUitl.showLong(msg);
    }

    @Override
    public void showLongToast(int resId) {
        ToastUitl.showLong(resId);
    }

    @Override
    public void showShortToast(String msg) {

        ToastUitl.showShort(msg);
    }

    @Override
    public void showShortToast(int resId) {
        ToastUitl.showShort(resId);
    }

    @Override
    public void stopLoading() {


    }

    @Override
    public void showErrorTip(String msg) {

    }
}
