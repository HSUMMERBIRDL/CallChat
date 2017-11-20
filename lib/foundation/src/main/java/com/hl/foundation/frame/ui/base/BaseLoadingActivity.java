package com.hl.foundation.frame.ui.base;

import com.hl.foundation.frame.ui.BaseLoadingView;
import com.hl.foundation.frame.ui.BaseModel;
import com.hl.foundation.frame.ui.BasePresenter;
import com.hl.foundation.library.widget.LoadingDialog;
import com.hl.foundation.library.widget.ToastUitl;

/**
 * des:
 * Created by HL
 * on 2017-05-02.
 */

public abstract class BaseLoadingActivity<M extends BaseModel,P extends BasePresenter> extends BaseActivity<M,P> implements BaseLoadingView {

    @Override
    public void showLoading() {

        LoadingDialog.showDialogForLoading(this,"",true);
    }

    @Override
    public void showLoading(String msg) {
        LoadingDialog.showDialogForLoading(this,msg,true);
    }

    @Override
    public void showNotCancelLoading(String msg) {
        LoadingDialog.showDialogForLoading(this,msg,false);
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
