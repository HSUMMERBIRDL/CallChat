package com.hl.foundation.frame.app;

import android.app.Application;
import android.content.Context;

/**
 * des:
 * Created by HL
 * on 2017-04-24.
 */

public class BaseApplication extends Application {

    private static BaseApplication baseApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
    }


    public static Context getAppContext() {
        return baseApplication;
    }


}
