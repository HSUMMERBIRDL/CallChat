package com.nesun.callchat.basis.app;

import com.hl.foundation.frame.app.BaseApplication;
import com.hl.foundation.library.utils.LogUtils;
import com.nesun.callchat.basis.AppConfig;

/**
 * des:
 * Created by HL
 * on 2017-07-26.
 */

public class AppApplication extends BaseApplication {

    private static AppApplication baseApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        LogUtils.logInit(AppConfig.isDebug);
    }
}
