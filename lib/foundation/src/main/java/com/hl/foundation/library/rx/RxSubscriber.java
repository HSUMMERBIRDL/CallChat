package com.hl.foundation.library.rx;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.hl.foundation.R;
import com.hl.foundation.frame.app.BaseApplication;
import com.hl.foundation.library.exception.ApiException;
import com.hl.foundation.library.utils.LogUtils;
import com.hl.foundation.library.utils.NetWorkUtils;
import com.hl.foundation.library.utils.StringUtils;
import com.hl.foundation.library.widget.LoadingDialog;
import com.hl.foundation.library.widget.ToastUitl;

import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * des:订阅封装
 * Created by xsf
 * on 2016.09.10:16
 */

public abstract class RxSubscriber<T> extends Subscriber<T> {

    private static final String TAG = RxSubscriber.class.getSimpleName();
    private Context mContext;
    private String msg;
    private boolean showDialog=true;
    private String uuid;

    /**
     * 是否显示浮动dialog
     */
    public void showDialog() {
        this.showDialog= true;
    }
    public void hideDialog() {
        this.showDialog= true;
    }


    public RxSubscriber(){

    }

    public RxSubscriber(Context context, String msg, boolean showDialog) {
        this.mContext = context;
        this.msg = msg;
        this.showDialog=showDialog;
    }
    public RxSubscriber(Context context) {
        this(context, BaseApplication.getAppContext().getString(R.string.loading),true);
    }
    public RxSubscriber(Context context, boolean showDialog) {
        this(context, BaseApplication.getAppContext().getString(R.string.loading),showDialog);
    }

    public RxSubscriber(Context context, boolean showDialog, String uuid) {
        this(context, BaseApplication.getAppContext().getString(R.string.loading),showDialog);
        this.uuid = uuid;
    }

    @Override
    public void onCompleted() {
        if (showDialog)
            LoadingDialog.cancelDialogForLoading();
    }
    @Override
    public void onStart() {
        super.onStart();
        if (showDialog) {

            if(null != mContext){
                if(mContext instanceof Activity){
                    LoadingDialog.showDialogForLoading((Activity) mContext,msg,true);
                }
            }
        }
    }


    @Override
    public void onNext(T t) {

        //  在这里具体进行业务的代码处理

        if(!StringUtils.isEmpty(uuid)){

        }
        if(null != t){
            LogUtils.logi("HttpApi",t.toString());
        }else{
            LogUtils.logi("HttpApi","服务端没有返回datas数据");
        }

        _onNext(t);
    }


    @Override
    public void onError(Throwable e) {

        LogUtils.loge("HttpApi",e);
        Log.e("HttpApi",e.toString());

        if (showDialog)
            LoadingDialog.cancelDialogForLoading();

        String message = "";
        int code = -1234;

        //网络
        if (!NetWorkUtils.isNetConnected(BaseApplication.getAppContext())) {
            message = BaseApplication.getAppContext().getString(R.string.no_net);
        }
        //服务器
//        else if (e instanceof ServerException) {
//            message = e.getMessage();
//        }

        // 业务错误 可以在这里错误的预处理
        else if(e instanceof ApiException){

            code = ((ApiException) e).getCode();

            if(code == 500){
                message = "接口异常";
            }else{
                message = e.getMessage();
            }
        }

        else if(e instanceof SocketTimeoutException){

            message = BaseApplication.getAppContext().getString(R.string.time_out);
        }

        else if(e instanceof HttpException){
            code = ((HttpException) e).code();
            message = e.getMessage();
        }

        //其它
        else {
            message = BaseApplication.getAppContext().getString(R.string.unknow_error);
//            message = e.toString();
        }
        _onError(message,code);


    }

    protected abstract void _onNext(T t);

    protected  void _onError(String message,int code){

        if(!StringUtils.isEmpty(message)){
            ToastUitl.showLong(message);
        }
    }

}
