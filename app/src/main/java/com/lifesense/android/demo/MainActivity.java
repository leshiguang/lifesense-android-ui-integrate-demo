package com.lifesense.android.demo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.lifesense.android.health.service.LZHealth;
import com.lifesense.android.health.service.Page;
import com.lifesense.weidong.lswebviewmoudle.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
    }

    protected void initData() {
        if(checkPermission()){
            String androidId = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            LZHealth.getInstance().login(androidId, loginState -> Toast.makeText(this,loginState.name(),Toast.LENGTH_SHORT).show());
        }
        findViewById(R.id.btStep).setOnClickListener(v -> LZHealth.getInstance().openPage(Page.STEP));
        findViewById(R.id.btBloodPressure).setOnClickListener(v -> LZHealth.getInstance().openPage(Page.BLOOD_PRESSURE));
        findViewById(R.id.btHr).setOnClickListener(v -> LZHealth.getInstance().openPage(Page.HR));
        findViewById(R.id.btWeight).setOnClickListener(v -> LZHealth.getInstance().openPage(Page.WEIGHT));
        findViewById(R.id.btSleep).setOnClickListener(v -> LZHealth.getInstance().openPage(Page.SLEEP));
        findViewById(R.id.btDeviceList).setOnClickListener(v -> LZHealth.getInstance().openPage(Page.DEVICE_LIST));
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200) {
            if (grantResults != null) {
                boolean flag = true;
                String leakPermission = "";
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        leakPermission = permissions[i];
                        flag = false;
                    }
                }
                if (flag) {
                    String androidId = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                    LZHealth.getInstance().login(androidId, loginState -> {
                        Toast.makeText(this,loginState.name(),Toast.LENGTH_SHORT);
                    });

                    finish();
                } else {
                    Toast.makeText(this,"缺少权限:" + leakPermission, Toast.LENGTH_SHORT).show();
                }
            }

        }
    }


    private boolean checkPermission() {
        return checkReadPermission(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_SMS, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.BLUETOOTH, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION}, 200);
    }


    /**
     * 判断是否有某项权限
     *
     * @param string_permission 权限
     * @param request_code      请求码
     * @return
     */
    public boolean checkReadPermission(String[] string_permission, int request_code) {
        boolean flag = true;
        for (int i = 0; i < string_permission.length; i++) {
            if (ContextCompat.checkSelfPermission(this, string_permission[i]) != PackageManager.PERMISSION_GRANTED) {//已有权限
                flag = false;
                break;
            }
        }
        if(flag) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, string_permission, request_code);
            return false;
        }
    }

}