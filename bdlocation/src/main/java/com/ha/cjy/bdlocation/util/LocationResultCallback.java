package com.ha.cjy.bdlocation.util;

import com.ha.cjy.bdlocation.bean.LocationInfo;

/**
 * 定位结果回调
 * Created by cjy on 2018/8/28.
 */

public interface LocationResultCallback {
    void onLocationResult(LocationInfo info);
}
