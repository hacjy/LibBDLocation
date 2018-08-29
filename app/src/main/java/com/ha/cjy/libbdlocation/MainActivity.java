package com.ha.cjy.libbdlocation;

import android.annotation.TargetApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.ha.cjy.bdlocation.bean.LocationInfo;
import com.ha.cjy.bdlocation.util.BDLocationManager;
import com.ha.cjy.bdlocation.util.LocationPremissionUtil;
import com.ha.cjy.bdlocation.util.LocationResultCallback;

/**
 * 测试定位功能
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mBtnLocation;
    private TextView mTvLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
    }

    private void initView(){
        mBtnLocation = findViewById(R.id.btn_location);
        mTvLocation = findViewById(R.id.tv_location);
    }

    private void initListener(){
        mBtnLocation.setOnClickListener(this);
        BDLocationManager.requestionPermission(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        restartLocation();
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LocationPremissionUtil.LOCATION_PERMISSION_REQUEST){
            if (LocationPremissionUtil.isAllGranted(grantResults)){
                //进行定位
                restartLocation();
            }else{
                Toast.makeText(MainActivity.this,"请到应用设置中打开位置权限",Toast.LENGTH_SHORT).show();
                LocationPremissionUtil.toSettingActivity(MainActivity.this);
            }
        }
    }

    private void restartLocation(){
        BDLocationManager.restartLocation(MainActivity.this,new LocationResultCallback() {
            @Override
            public void onLocationResult(LocationInfo info) {
                if (info != null) {
                    mTvLocation.setText(info.province + info.city);
                }
            }
        });
    }

    private void startLocation(){
        BDLocationManager.startLocation(MainActivity.this,new LocationResultCallback() {
            @Override
            public void onLocationResult(LocationInfo info) {
                if (info != null) {
                    mTvLocation.setText(info.province + info.city);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        startLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BDLocationManager.stopLocation(this);
    }
}
