package com.ha.cjy.bdlocation.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * 定位相关的权限处理工具
 * Created by cjy on 2018/8/28.
 */

public class LocationPremissionUtil {
    public static final int LOCATION_PERMISSION_REQUEST = 127;

    /**
     * 是否打开了系统GPS定位（位置信息）开关
     * @param context
     * @return
     */
    public static boolean isOpenGps(Context context){
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    /**
     * 获取权限列表
     * @param context
     */
    @TargetApi(23)
    public static boolean getPersimmions(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String permissionInfo = "";

            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if(context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if(context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
			/*
			 * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(context,permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(context,permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                ((Activity)context).requestPermissions(permissions.toArray(new String[permissions.size()]), LOCATION_PERMISSION_REQUEST);
                return false;
            }else{
                return true;
            }
        }else{
            return true;
        }
    }

    @TargetApi(23)
    private static boolean addPermission(Context context,ArrayList<String> permissionsList, String permission) {
        // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
        if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            //shouldShowRequestPermissionRationale上次请求过，但是拒绝了，返回true，其他情况返回false
            if ( ((Activity)context).shouldShowRequestPermissionRationale(permission)){
                return true;
            }else{
                permissionsList.add(permission);
                return false;
            }

        }else{
            return true;
        }
    }

    /**
     * 判断是否权限全部授权了
     * @param grantResults
     * @return
     */
    public static boolean isAllGranted(int[] grantResults){
        boolean isAllGranted = true;
        if (grantResults != null && grantResults.length > 0){
            for (int i = 0; i< grantResults.length; i++){
                if (grantResults[i] == PackageManager.PERMISSION_DENIED){
                    isAllGranted = false;
                    break;
                }else{
                    isAllGranted = true;
                }
            }
        }
        return isAllGranted;
    }

    /**
     * 跳转到应用的设置页面
     */
    public static void toSettingActivity(Activity activity){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, LOCATION_PERMISSION_REQUEST);
    }
}
