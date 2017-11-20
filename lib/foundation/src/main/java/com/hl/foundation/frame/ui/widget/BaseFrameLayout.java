package com.hl.foundation.frame.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import java.util.Map;

/**
 * Created by HL on 2016/3/4.
 */
public abstract class BaseFrameLayout extends FrameLayout {

    protected  Context mContext;
    protected  View  root;


    public BaseFrameLayout(Context context) {
        this(context, null);
    }

    public BaseFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        initAttrs(attrs,defStyleAttr);
        create();
    }

    public  void create(){


        inflateRootView();
        initDatas();
        initViews();
        initEvents();
        setUpDatas();
    }

    protected void initAttrs( AttributeSet attrs, int defStyleAttr) {

    }


    private void inflateRootView() {

        if(setRootViewId() != 0){
            root = View.inflate(getContext(), setRootViewId(), this);
        }
    }

    protected abstract int setRootViewId();

    protected  void initDatas(){

    };

    protected abstract void initViews();

    protected abstract void initEvents();

    /**
     * 此时view 月 相关的datas 都以准备完成
     * 可以进行view与datas的相关绑定了
     */
    protected  void setUpDatas(){

    };

    /**
     * 简化findViewById
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T $(int viewId) {
        return (T) findViewById(viewId);
    }

    /**
     * 简化findViewById（从指定view寻找）
     *
     * @param view
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T $(View view, int viewId) {
        return (T) view.findViewById(viewId);
    }

    /**
     * 跳转至指定Activity（无参）
     * @param cls
     */
    protected void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 跳转至指定Activity（带参）
     * @param cls
     * @param params
     */
    protected void startActivity(Class<?> cls, Map<String, Object> params) {
        Intent intent = new Intent(getContext(), cls);
        if(null != params) {
            for(String key : params.keySet()) {
                intent.putExtra(key, String.valueOf(params.get(key)));
            }
        }
        getContext().startActivity(intent);
    }



}
