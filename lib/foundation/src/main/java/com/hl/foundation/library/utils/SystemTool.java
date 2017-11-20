///*
// * Copyright (c) 2014, KJFrameForAndroid 张涛 (kymjs123@gmail.com).
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.hl.foundation.library.utils;
//
//import android.content.Context;
//import android.content.pm.ApplicationInfo;
//import android.content.pm.PackageManager;
//import android.content.pm.PackageManager.NameNotFoundException;
//import android.content.res.Resources;
//import android.os.Build;
//import android.provider.Settings.Secure;
//import android.telephony.TelephonyManager;
//import android.text.TextUtils;
//import android.util.DisplayMetrics;
//
//import com.hl.foundation.frame.app.BaseApplication;
//
//
///**
// * 系统信息工具类
// *
// * Created by FreddyChen on 2016/3/30.
// */
//public final class SystemTool {
//
//
//    public static int[] getResolution(){
//
//        int[] resolutions = new int[2];
//
//        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
//        resolutions[0] = displayMetrics.heightPixels;
//        resolutions[1] = displayMetrics.widthPixels;
//        return resolutions;
//    }
//
//    /**
//     * 获取手机IMEI码
//     */
//    public static String getPhoneIMEI() {
//        String id;
//        TelephonyManager tm = (TelephonyManager) BaseApplication.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
//        id = tm.getDeviceId();
//
//        if (TextUtils.isEmpty(id)) {
//            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) BaseApplication.getAppContext().getSystemService(Context.WIFI_SERVICE);
//            String mac = wifi.getConnectionInfo().getMacAddress();
//            id = mac;
//        }
//
//        if (TextUtils.isEmpty(id)) {
//            //如果Android Pad没有IMEI,用此方法获取设备ANDROID_ID：
//            id = Secure.getString(BaseApplication.getAppContext().getContentResolver(), Secure.ANDROID_ID);
//        }
//
//        return id;
//    }
//
//    /**
//     * 获取运营商sim卡imsi号
//     */
//    public static String getPhoneIMSI() {
//        TelephonyManager tm = (TelephonyManager) BaseApplication.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
//        String imsi = tm.getSubscriberId();
//        if (imsi == null) {
//            imsi = "-1";
//        }
//        return imsi;
//    }
//
//    /**
//     * 获取运营商名称
//     *
//     * @return CMCC、UNICOM、TELECOM
//     */
//    public static String getSimOperator() {
//        TelephonyManager telManager = (TelephonyManager) BaseApplication.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
//        String operator = telManager.getSimOperator();
//        if (operator != null) {
//            if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {
//                //中国移动
//                return "CMCC";
//            } else if (operator.equals("46001")) {
//                //中国联通
//                return "UNICOM";
//            } else if (operator.equals("46003")) {
//                //中国电信
//                return "TELECOM";
//            }
//        }
//        return "";
//    }
//
//    /**
//     * 获取手机系统SDK版本
//     *
//     * @return 如API 17 则返回 17
//     */
//    public static int getSDKVersion() {
//        return Build.VERSION.SDK_INT;
//    }
//
//    /**
//     * 获取系统版本
//     *
//     * @return 形如2.3.3
//     */
//    public static String getSystemVersion() {
//        return Build.VERSION.RELEASE;
//    }
//
//    /**
//     * 获取机型
//     *
//     * @return
//     */
//    public static String getDeviceModel() {
//        return Build.MODEL;
//    }
//
//    /**
//     * 获取系统厂商
//     *
//     * @return Xiaomi/samsung等
//     */
//    public static String getMaunfacturer() {
//        return Build.MANUFACTURER;
//    }
//
//    /**
//     * 获取当前应用的版本号
//     *
//     * @return version code
//     */
//    public static int getAPPVersionCode() {
//        int verstionCode = 0;
//        try {
//            verstionCode = BaseApplication.getAppContext().getPackageManager().getPackageInfo(BaseApplication.getAppContext().getPackageName(), 0).versionCode;
//        } catch (NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return verstionCode;
//    }
//
//    private static String versionName;
//
//    /**
//     * 获取当前应用的版本名
//     *
//     * @return version name
//     */
//    public static String getAPPVersionName() {
//        if (TextUtils.isEmpty(versionName)) {
//            try {
//                versionName = BaseApplication.getAppContext().getPackageManager()
//                        .getPackageInfo(BaseApplication.getAppContext().getPackageName(), 0).versionName;
//            } catch (NameNotFoundException e) {
//                versionName = "";
//                e.printStackTrace();
//            }
//        }
//        return versionName;
//    }
//
//    /**
//     * 获取应用渠道信息
//     * @param context
//     * @param key 键，根据key获取对应的value
//     * @return
//     */
//    public static String getChannelName(Context context, String key) {
//        if(null == context || TextUtils.isEmpty(key))
//            return null;
//
//        String resultData = null;
//        try {
//            PackageManager packageManager = context.getPackageManager();
//            if(null != packageManager) {
//                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
//                if(null != applicationInfo && null != applicationInfo.metaData) {
//                    resultData = applicationInfo.metaData.getString(key);
//                }
//            }
//        } catch (NameNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        return resultData;
//    }
//
//    public static boolean isFroyoOrLater() {
//        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
//    }
//
//    public static boolean isGingerbreadOrLater() {
//        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
//    }
//
//    public static boolean isHoneycombOrLater() {
//        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
//    }
//
//    public static boolean isHoneycombMR1OrLater() {
//        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
//    }
//
//    public static boolean isICSOrLater() {
//        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
//    }
//
//    public static boolean isJellyBeanOrLater() {
//        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
//    }
//
//    public static boolean isJellyBeanMR1OrLater() {
//        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
//    }
//
//    public static boolean isJellyBeanMR2OrLater() {
//        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
//    }
//
//    public static boolean isKitKatOrLater() {
//        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
//    }
//
//    public static boolean isLollipopOrLater() {
//        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
//    }
//
////    public static boolean isMarshmallowOrLater() {
////        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M;
////    }
//}