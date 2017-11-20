package com.hl.foundation.frame.ui;

import android.content.Context;

import com.hl.foundation.library.rx.RxManager;

/**
 * des:
 * Created by HL
 * on 2017-04-25.
 */

public abstract class BasePresenter<M,V> {
    public  final  String  TAG = this.getClass().getSimpleName();


    protected M mModel;
    protected V mView;


    public Context mContext;
    protected RxManager mRxManager = new RxManager();

    public void setMV(M m,V v){
        this.mModel = m;
        this.mView = v;

        this.onStart();
    }

    protected void onStart(){

    }

    public void onDestory() {

        mRxManager.clear();
        mView = null;

    }
}
