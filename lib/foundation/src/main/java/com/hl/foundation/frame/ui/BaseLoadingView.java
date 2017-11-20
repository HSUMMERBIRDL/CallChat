package com.hl.foundation.frame.ui;

/**
 * des: 基本的加载界面
 * Created by HL
 * on 2017-05-02.
 */

public interface BaseLoadingView extends BaseView{

    void showLoading();
    void showLoading(String msg);
    void showNotCancelLoading(String msg);
    void dismissLoading();
    void showLongToast(String msg);
    void showLongToast(int resId);
    void showShortToast(String msg);
    void showShortToast(int resId);

    void stopLoading();
    void showErrorTip(String msg);

}
