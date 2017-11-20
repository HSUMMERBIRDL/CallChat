package com.hl.foundation.library.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

/**
 * des:
 * Created by HL
 * on 2017/2/15 0015.
 */

public class AndroidUtils {

    /**
     * 直接打电话
     * @param phoneNum
     * @param content
     */

    public static void callPhone(String phoneNum, Context content) {

        if (!StringUtils.isEmpty(phoneNum)) {

            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + phoneNum);
            intent.setData(data);

            if (ActivityCompat.checkSelfPermission(content, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            content.startActivity(intent);
        }
    }

    /**
     * 拨号界面 并没有直接打
     * @param phoneNum
     * @param content
     */
    public static void goCallPhone(String phoneNum,Context content){

        if(!StringUtils.isEmpty(phoneNum)){

            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + phoneNum);
            intent.setData(data);
//            if (ActivityCompat.checkSelfPermission(content, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
            content.startActivity(intent);
        }
    }


    /**
     * 获取当前应用的本地版本号
     * @return
     */
    public static String getLocalVersion(Context ctx) {
        String versionName = null;
        try {
            PackageInfo pinfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            versionName = pinfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            // 获取不到应用程序包，直接返回空
            // ignore
        }

        return versionName;
    }

}
