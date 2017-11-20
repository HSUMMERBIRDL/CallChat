package com.hl.foundation.library.utils;

import android.view.View;
import android.view.animation.Animation;

import com.hl.foundation.R;

/**
 * Created by HL on 2016/3/27.
 */
public class AnimationUtils {

    /**
     * 从上往下进入
     * @param up
     */
    public static void showSlideTopIn(View up) {

        if ( up != null) {
            if (up.getVisibility() == View.GONE) {
                up.setVisibility(View.VISIBLE);
            }
            Animation animation = android.view.animation.AnimationUtils.loadAnimation(up.getContext(), R.anim.anima_slide_top_in);
            up.startAnimation(animation);
        }
    }

    /**
     * 从下往上退出
     * @param view
     */
    public static void hideSlideButtomOut(View view) {

        if ( view != null) {
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.GONE);
            }
            Animation animation = android.view.animation.AnimationUtils.loadAnimation(view.getContext(), R.anim.anima_slide_buttom_out);
            view.startAnimation(animation);
        }
    }

}
