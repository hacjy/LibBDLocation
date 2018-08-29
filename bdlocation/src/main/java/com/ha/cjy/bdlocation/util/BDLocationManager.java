package com.ha.cjy.bdlocation.util;

import android.content.Context;
import android.os.Build;

/**
 * 百度定位统一管理类
 * 提供给外部调用
 * Created by cjy on 2018/8/28.
 */

public class BDLocationManager {

    /**
     * 开始定位
     * @param context
     */
    public static void startLocation(Context context,LocationResultCallback callback){
        //GPS定位开关
//        if (!LocationPremissionUtil.isOpenGps(context)){
//            Toast.makeText(context, "请打开位置信息服务，否则定位会失败", Toast.LENGTH_SHORT).show();
//            return;
//        }
        //根据版本判定，是请求权限还是开始定位
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //请求权限
           boolean isGranted =  requestionPermission(context);
           if (isGranted){
               getLocation(context,callback);
           }
        }else{
            getLocation(context,callback);
        }
    }

    /**
     * 重新启动定位
     * @param context
     */
    public static void restartLocation(Context context,LocationResultCallback callback){
        //定位的回调
        BDLocaiontUtil.getIntance(context).setCallback(callback);
        //重新定位
        BDLocaiontUtil.getIntance(context).restartLocation();
    }


    private static void getLocation(Context context,LocationResultCallback callback){
        //定位的回调
        BDLocaiontUtil.getIntance(context).setCallback(callback);
        //开始定位
        BDLocaiontUtil.getIntance(context).startLocation();
    }

    /**
     * 请求权限
     * @param context
     */
    public static boolean requestionPermission(Context context){
        return LocationPremissionUtil.getPersimmions(context);
    }

    /**
     * 结束定位
     * @param context
     */
    public static void stopLocation(Context context){
        BDLocaiontUtil.getIntance(context).stopLocation();
    }
}
