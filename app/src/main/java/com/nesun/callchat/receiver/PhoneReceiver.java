package com.nesun.callchat.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import com.hl.foundation.library.utils.CThreadUtils;
import com.hl.foundation.library.utils.LogUtils;
import com.hl.foundation.library.utils.StringUtils;
import com.nesun.callchat.data.vo.PhoneInfo;
import com.nesun.callchat.ui.activity.MainActivity;

/**
 * des:
 * Created by HL
 * on 2017-07-27.
 */

public class PhoneReceiver extends BroadcastReceiver {

    private static final java.lang.String TAG = PhoneReceiver.class.getSimpleName();

    private String phoneNum = ""; // 别人打电话进来和自己打电话出去 拿电话号码的方式是不同的

    private boolean isSelfCallOut = false; // 是否是自己打出去的情况

    @Override
    public void onReceive(Context context, Intent intent) {


        String action = intent.getAction();

        PhoneInfo phoneInfo = new PhoneInfo();
        phoneInfo.name = "勒布朗.詹姆斯";
        phoneInfo.phoneNum = "18118888888";

        Bundle extras = intent.getExtras();
        LogUtils.logi(TAG,"extras: " + extras.toString());

//        String phoneNum = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
//        phoneInfo.phoneNum = phoneNum;
//        LogUtils.logi(TAG,"phoneNum: " + phoneNum);
        if (StringUtils.equals(action, Intent.ACTION_NEW_OUTGOING_CALL)) {

            String phoneNumber  = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);  // 自己打电话出去 号码是这么拿的  这里获取的是你将要打出去的电话号码
            phoneInfo.phoneNum = phoneNumber;
//            isSelfCallOut = true;
            doReceivePhone(context,phoneInfo);
        } else {
            TelephonyManager tManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            switch (tManager.getCallState()) {

                case TelephonyManager.CALL_STATE_RINGING:          //  2
                    LogUtils.logi(TAG,"别人打电话来了");
                    String phoneNumber = intent.getStringExtra("incoming_number");
                    phoneInfo.phoneNum = phoneNumber;
                    doReceivePhone(context,phoneInfo);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:   //  3

                    break;
                case TelephonyManager.CALL_STATE_IDLE:  //  4
                    break;
            }
        }
    }

    private void doReceivePhone(final Context context,final PhoneInfo phoneInfo) {

        CThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                MainActivity.startActivity(context,phoneInfo);
            }
        },1000*2);

    }
}
