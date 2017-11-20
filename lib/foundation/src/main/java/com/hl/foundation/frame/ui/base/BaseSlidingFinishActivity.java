package com.hl.foundation.frame.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.hl.foundation.R;
import com.hl.foundation.frame.ui.BaseModel;
import com.hl.foundation.frame.ui.BasePresenter;
import com.hl.foundation.frame.ui.widget.SwipeBackLayout;

/**
 * Created by ${Stephen} on 2017-05-16.
 */

public abstract class BaseSlidingFinishActivity<M extends BaseModel, P extends BasePresenter>
        extends BaseLoadingActivity<M, P> {
    private SwipeBackLayout swipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        swipeBackLayout = (SwipeBackLayout) LayoutInflater.from(this).inflate(R.layout
                .activity_slidingfinish, null);
        swipeBackLayout.attachToActivity(this);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
    }




    // Press the back button in mobile phone
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        finish();
    }
}
