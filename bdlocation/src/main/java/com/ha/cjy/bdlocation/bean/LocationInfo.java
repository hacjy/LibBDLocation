package com.ha.cjy.bdlocation.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.baidu.location.BDLocation;

/**
 * 地址信息
 * Created by cjy on 2018/8/28.
 */

public class LocationInfo implements Parcelable {
    public BDLocation location;

    public double latitude; //纬度
    public double lontitude; //经度
    public String addr;    //获取详细地址信息
    public String country;    //获取国家
    public String province ;    //获取省份
    public String city ;    //获取城市
    public String district;    //获取区县
    public String street;    //获取街道信息

    public LocationInfo(BDLocation location) {
        this.location = location;
    }

    protected LocationInfo(Parcel in) {
        location = in.readParcelable(BDLocation.class.getClassLoader());
        latitude = in.readDouble();
        lontitude = in.readDouble();
        addr = in.readString();
        country = in.readString();
        province = in.readString();
        city = in.readString();
        district = in.readString();
        street = in.readString();
    }

    public static final Creator<LocationInfo> CREATOR = new Creator<LocationInfo>() {
        @Override
        public LocationInfo createFromParcel(Parcel in) {
            return new LocationInfo(in);
        }

        @Override
        public LocationInfo[] newArray(int size) {
            return new LocationInfo[size];
        }
    };

    public void setLocationInfo(double latitude, double lontitude, String addr,
                                String country, String province, String city, String district, String street) {
        this.latitude = latitude;
        this.lontitude = lontitude;
        this.addr = addr;
        this.country = country;
        this.province = province;
        this.city = city;
        this.district = district;
        this.street = street;
    }

    public LocationInfo(double latitude, double lontitude,String addr, String country, String province, String city, String district, String street) {
        this.latitude = latitude;
        this.lontitude = lontitude;
        this.addr = addr;
        this.country = country;
        this.province = province;
        this.city = city;
        this.district = district;
        this.street = street;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeParcelable(location, flags);
        dest.writeDouble(latitude);
        dest.writeDouble(lontitude);
        dest.writeString(addr);
        dest.writeString(country);
        dest.writeString(province);
        dest.writeString(city);
        dest.writeString(district);
        dest.writeString(street);
    }

}
