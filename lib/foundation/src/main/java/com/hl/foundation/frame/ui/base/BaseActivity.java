package com.hl.foundation.frame.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;

import com.hl.foundation.R;
import com.hl.foundation.frame.ui.BaseModel;
import com.hl.foundation.frame.ui.BasePresenter;
import com.hl.foundation.frame.ui.widget.SwipeBackLayout;
import com.hl.foundation.library.manager.AppManager;
import com.hl.foundation.library.rx.RxManager;
import com.hl.foundation.library.utils.TUtil;
import com.hl.foundation.library.utils.ToolbarUtil;

import butterknife.ButterKnife;

/**
 * des:
 * Created by HL
 * on 2017-04-25.
 */

public abstract class BaseActivity<M extends BaseModel,P extends BasePresenter> extends Activity {

    public final String TAG =  this.getClass().getSimpleName();
    public RxManager mRxManager;
    private AppManager appManager;

    protected M mModel;
    protected P mPresenter;

    protected Context mContext;

    private SwipeBackLayout swipeBackLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRxManager = new RxManager();
        appManager = AppManager.getAppManager();

        mContext = this;
        mModel =  TUtil.getT(this, 0);
        mPresenter =  TUtil.getT(this, 1);

        if(null != mPresenter){
            initPresenter();
        }

        //  不能放在下面 实现将activity划出去的功能
//        swipeBackLayout = (SwipeBackLayout) LayoutInflater.from(this).inflate(R.layout
//                .activity_slidingfinish, null);
//        swipeBackLayout.attachToActivity(this);

        beforeSetContentView();
        setContentView(getLayoutId());
        afterSetContentView();


        ButterKnife.bind(this);

        initDatas();
        initViews();

        initEvents();


    }

    private void initPresenter() {

        mPresenter.mContext = this;
        mPresenter.setMV(mModel,this);
    }


    protected void beforeSetContentView() {
        appManager.addActivity(this);
        // 无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if(isFullScreen()){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    protected boolean isFullScreen() {
        return false;
    }


    //
    protected void afterSetContentView() {
        // 设置沉浸式状态栏

//        ToolbarUtil.setColorNoTranslucent(this, ContextCompat.getColor(this, getStatuBarColor()));

        if(!isFullScreen()){
            if(isImmersiveBar()){
                ToolbarUtil.setColorNoTranslucent(this, ContextCompat.getColor(this, getStatuBarColor()));
            }
        }

    }

    protected int getStatuBarColor() {
        return R.color.colorTheme;
    }

    protected boolean isImmersiveBar() {
        return true;
    }

    protected int getTitleBarColor(){
        return -1;
    };

    protected abstract void initDatas();

    protected abstract int getLayoutId();

    protected abstract void initViews();

    protected abstract void initEvents();




    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(null != mPresenter){
            mPresenter.onDestory();
        }
        mRxManager.clear();
        ButterKnife.unbind(this);
        appManager.finishActivity(this);
    }
}
