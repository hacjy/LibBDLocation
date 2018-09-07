package com.ha.cjy.bdlocation.util;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClientOption;
import com.ha.cjy.bdlocation.bean.LocationInfo;

/**
 * 百度定位工具类
 * Created by cjy on 2018/8/27.
 */

public class BDLocaiontUtil {
    private static volatile BDLocaiontUtil mInstance;

    //百度定位配置
    private LocationService locationService;
    private Context mContext;
    //定位结果回调
    private LocationResultCallback mCallback;

    private BDLocaiontUtil(){

    }

    private BDLocaiontUtil(Context context){
        mContext = context;
        init();
    }

    public static BDLocaiontUtil getIntance(Context context){
        if (mInstance == null){
            synchronized (BDLocaiontUtil.class){
                if (mInstance == null){
                    mInstance  = new BDLocaiontUtil(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化定位sdk
     */
    private void init(){
        locationService = new LocationService(mContext);
        //获取locationservice实例，建议应用中只初始化1个location实例
        locationService.registerListener(mListener);
        locationService.start();
    }

    /**
     * 自定义定位配置
     * @param option
     */
    public void setLocationOption(LocationClientOption option){
        locationService.setLocationOption(option);
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        //注销掉监听
        locationService.unregisterListener(mListener);
        //停止定位服务
        locationService.stop();
    }

    /**
     * 重新定位
     */
    public void restartLocation() {
        locationService.restart();
    }


    /**
     * 开始定位
     */
    public void startLocation() {
        startLocation(0);
    }

    /**
     * 开始定位
     * @param type
     */
    public void startLocation(int type) {
        //注册监听
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }

        //定位SDK start之后立即执行，这种情况下很难定位成功，因为定位SDK刚开始启动还没有获取到定位信息。这时getLocation一般为null。如果是要获取位置成功，可以在listerner中添加一个判断如果strData为空，则再发起一次定位；
        locationService.start();
    }

    public void setCallback(LocationResultCallback callback){
        mCallback = callback;
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法
     *
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            LocationInfo info =  new LocationInfo(location);
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                double latitude = location.getLatitude();//经度
                double longitude = location.getLongitude();//纬度
                location.setLatitude(latitude);
                location.setLongitude(longitude);

                String addr = location.getAddrStr();    //获取详细地址信息
                if (addr == null){
                    startLocation();
                    return;
                }
                String country = location.getCountry();    //获取国家
                String province = location.getProvince();    //获取省份
                String city = location.getCity();    //获取城市
                String district = location.getDistrict();    //获取区县
                String street = location.getStreet();    //获取街道信息

                info.setLocationInfo(latitude,longitude,addr,country,province,city,district,street);
            }
            if (mCallback != null){
                mCallback.onLocationResult(info);
            }
        }
    };
}
