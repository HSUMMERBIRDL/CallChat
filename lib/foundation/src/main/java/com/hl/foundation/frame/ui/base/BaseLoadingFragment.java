package com.hl.foundation.frame.ui.base;

import com.hl.foundation.frame.ui.BaseLoadingView;
import com.hl.foundation.frame.ui.BaseModel;
import com.hl.foundation.frame.ui.BasePresenter;
import com.hl.foundation.library.widget.LoadingDialog;
import com.hl.foundation.library.widget.ToastUitl;

/**
 * des:
 * Created by HL
 * on 2017-05-03.
 */

public  abstract class BaseLoadingFragment<M extends BaseModel,P extends BasePresenter> extends BaseFragment<M,P> implements BaseLoadingView {


    @Override
    public void showLoading() {

        LoadingDialog.showDialogForLoading(this.getActivity(),"",true);
    }

    @Override
    public void showLoading(String msg) {
        LoadingDialog.showDialogForLoading(this.getActivity(),msg,true);
    }

    @Override
    public void showNotCancelLoading(String msg) {
        LoadingDialog.showDialogForLoading(this.getActivity(),msg,false);
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
}
