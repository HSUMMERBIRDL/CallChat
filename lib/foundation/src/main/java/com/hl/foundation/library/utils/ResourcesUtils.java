package com.hl.foundation.library.utils;

import android.content.res.Resources;

import com.hl.foundation.frame.app.BaseApplication;

/**
 * des:
 * Created by HL
 * on 2017-05-08.
 */

public class ResourcesUtils {

    public static Resources getResoure() {
        return BaseApplication.getAppContext().getResources();
    }


    public static int getColor(int resid) {
        return getResoure().getColor(resid);
    }

    public static String getResourceString(int stringId){

        String s = "";

        s = BaseApplication.getAppContext().getResources().getString(stringId);
        return s;
    };

}
