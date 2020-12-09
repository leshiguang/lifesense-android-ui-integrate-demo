package com.lifesense.android.demo;

import android.Manifest;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;


import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lifesense.android.bluetooth.core.bean.BaseDeviceData;
import com.lifesense.android.bluetooth.core.bean.WifiInfo;
import com.lifesense.android.bluetooth.core.bean.constant.DeviceConnectState;
import com.lifesense.android.bluetooth.core.enums.DataType;
import com.lifesense.android.bluetooth.core.enums.WifiState;
import com.lifesense.android.bluetooth.core.interfaces.OnDeviceMeasureDataListener;
import com.lifesense.android.health.service.pages.PageDispatcher;
import com.lifesense.device.scale.application.interfaces.ILZDeviceService;
import com.lifesense.device.scale.application.interfaces.callback.BleReceiveCallback;
import com.lifesense.device.scale.application.interfaces.listener.OnDeviceConnectStateListener;
import com.lifesense.device.scale.application.service.LZDeviceService;
import com.lifesense.device.scale.login.LoginResponse;
import com.lifesense.weidong.lswebview.webview.LSWebViewManager;
import com.lifesense.weidong.lswebview.util.ToastUtil;


import com.lifesense.android.health.service.common.BaseActivity;

import com.lifesense.weidong.lswebviewmoudle.R;
import com.lifesense.weidong.lzsimplenetlibs.base.BaseResponse;
import com.lifesense.weidong.lzsimplenetlibs.common.ApplicationHolder;
import com.lifesense.weidong.lzsimplenetlibs.cookie.LZCookieManager;
import com.lifesense.weidong.lzsimplenetlibs.net.callback.IRequestCallBack;
import com.lifesense.weidong.lzsimplenetlibs.net.dispatcher.DefaultApiDispatcher;
import com.lifesense.weidong.lzsimplenetlibs.util.RequestCommonParamsUtils;

public class MainActivity extends BaseActivity {

    private static final String domain = "https://api-r1.leshiguang.com";
    private TextView tvWeight;
    private Button btDeviceList;

    private com.lifesense.android.bluetooth.core.bean.WifiInfo cache = new com.lifesense.android.bluetooth.core.bean.WifiInfo();
    private String mac;
    private BleReceiveCallback bleReceiveCallback;
    private JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {//未开启定位权限
            //开启定位权限,200是标识码
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {//未开启定位权限
            //开启定位权限,200是标识码
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED) {//未开启定位权限
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH}, 200);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_ADMIN)
                != PackageManager.PERMISSION_GRANTED) {//未开启定位权限
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, 200);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_PRIVILEGED)
                != PackageManager.PERMISSION_GRANTED) {//未开启定位权限
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_PRIVILEGED}, 200);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {//未开启定位权限
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_PRIVILEGED}, 200);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {//未开启定位权限
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_PRIVILEGED}, 200);
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {//未开启定位权限
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, 200);
        }

        setContentView(R.layout.activity_main);
        tvWeight = findViewById(R.id.tv_weight);
        btDeviceList = findViewById(R.id.toDeviceList);
        btDeviceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageDispatcher.openDevicePage(MainActivity.this);
            }
        });

        findViewById(R.id.btn_bp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageDispatcher.openBloodPressurePage(MainActivity.this);
            }
        });

        findViewById(R.id.btn_step).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageDispatcher.openStepPage(MainActivity.this);
            }
        });

        findViewById(R.id.resetWifi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LZDeviceService.getInstance().resetWifi(mac);
            }
        });

        findViewById(R.id.startScanWifi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ILZDeviceService deviceBindService = LZDeviceService.getInstance();
                deviceBindService.startScanWifi(mac);
            }
        });

        findViewById(R.id.startConfigWifi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LZDeviceService.getInstance().isConfigWifi(mac);
//                LZDeviceService.getInstance().startConfigWifi(LZDeviceService.getInstance().getBondedDevices().get(0).getMac(), "123456789", cache.getBssid(), cache.getStatus());
            }
        });


        findViewById(R.id.btn_sleep).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageDispatcher.openSleepPage(MainActivity.this);
            }
        });

        findViewById(R.id.btn_hr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageDispatcher.openHeartRatePage(MainActivity.this);
            }
        });


        TextView tvAppVersion = findViewById(R.id.tv_app_version);
        tvAppVersion.setText(getString(R.string.current_app_version, String.valueOf(ApplicationHolder.getAppVersionName())));

        findViewById(R.id.bt_weight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageDispatcher.openWeightPage(MainActivity.this);
            }
        });
        findViewById(R.id.bt_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                ToastUtil.showCustomCenterShowToast(MainActivity.this, "已退出登录!");
            }
        });
        findViewById(R.id.searchWifiConfig).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String s = LZDeviceService.getInstance().queryConfigedWifiSsid(mac);
//                Log.i("MainAct", s);
//
//                LZDeviceService.getInstance().getConnectedWifiInfo(mac);
            }
        });


    }

    @Override
    protected void initData(Bundle bundle) {
        DefaultApiDispatcher.changeDomain(domain);
        login();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (checkPermission()) {
                login();
            } else {
                ToastUtil.showSingletonToast(MainActivity.this, "缺少权限!");
            }
        }
    }

    private boolean checkPermission() {
        return checkReadPermission(new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
    }

    private void login() {
        String ANDROID_ID = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        //静默登录
        bleReceiveCallback = new BleReceiveCallback();
        //设置回调
        bleReceiveCallback.setConnectStateListener(new OnDeviceConnectStateListener() {
            @Override
            public void onDeviceConnectStateChange(String s, DeviceConnectState deviceConnectState) {

                Log.i("MainAct", Thread.currentThread().getName());

                Log.i("MainAct", deviceConnectState.name());
                mac = s;
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("MainAct", Thread.currentThread().getName());

                        DeviceConnectState deviceConnectState1 = LZDeviceService.getInstance().checkDeviceConnectState(s);
                        Log.i("MainAct", "check >>>" + deviceConnectState1.name());

                        tvWeight.append("onDeviceConnectStateChange==>" + deviceConnectState.name());
                    }
                });

            }

            @Override
            public void onReceiveWifiConnectState(WifiState wifiState) {
                Log.i("MainAct", wifiState.name());
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvWeight.append("onReceiveWifiConnectState==>" + wifiState.name());
                    }
                });
            }

            @Override
            public void onReceiveWifiScanEnd() {
                Log.i("scan end", "e");
                Log.i("onReceiveWifiScanEnd", "onReceiveWifiScanEnd");
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvWeight.append("onReceiveWifiScanEnd==>");
                    }
                });

            }

            @Override
            public void onReceiveWifiScanResult(WifiInfo wifiInfo) {
                Log.i("onReceiveWifiScanResult", wifiInfo.toString());
                if (wifiInfo.getSsid().contains("吴勇")) {
                    cache = wifiInfo;
                }
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvWeight.append("onReceiveWifiScanResult==>" + wifiInfo.getSsid());

                    }
                });

                Log.i("scan response", JSON.toJSONString(wifiInfo));
            }

            @Override
            public void onReceiveWifiConfigInfo(int i, String s) {
                Log.i("onReceiveWifiConfigInfo", s);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvWeight.append("onReceiveWifiConfigInfo==>" + s);
                    }
                });

            }
        });


        bleReceiveCallback.setReceiveListener(new OnDeviceMeasureDataListener() {
            @Override
            public void onReceiveDeviceMeasureData(DataType dataType, BaseDeviceData baseDeviceData) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvWeight.setText(JSON.toJSONString(baseDeviceData));
                    }
                });
            }

        });
        jsonObject = new JSONObject();
        try {
            ApplicationInfo appInfo = getPackageManager()
                    .getApplicationInfo(getPackageName(),
                            PackageManager.GET_META_DATA);

            String config = appInfo.metaData.getString("config");
            jsonObject = JSON.parseObject(config);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        LZDeviceService.getInstance().registerDataReceiveCallBack(bleReceiveCallback);
        LZDeviceService.getInstance().login(getApplicationContext(), jsonObject.getString("appKey"), jsonObject.getString("appSecret"), ANDROID_ID, new IRequestCallBack<BaseResponse>() {
            @Override
            public void onRequestSuccess(BaseResponse response) {
                LoginResponse loginResponse = (LoginResponse) response;
                RequestCommonParamsUtils.put("appVersion", "4.6");
                LZDeviceService.getInstance().startDataReceive();
                //初始化cookie
                LSWebViewManager.getInstance().init(loginResponse.getLoginEntity().getUserId(), loginResponse.getLoginEntity().getAccessToken(), jsonObject.getString("tn"));
                LSWebViewManager.getInstance().setDebug(true);
                LZDeviceService.getInstance().setDebug(true);
                ToastUtil.showCustomCenterShowToast(MainActivity.this, "登陆成功!");


            }

            @Override
            public void onRequestError(int i, String s, BaseResponse response) {
                Log.e("login---------", "msg : " + s + ", code : " + i);
            }
        });

    }


    public boolean isLogin() {
        return LZDeviceService.getInstance().getCurrentUser() != null;
    }

    public void logout() {
        LZCookieManager.getInstance().clearCookie();
        LSWebViewManager.getInstance().destroy();
        LZDeviceService.getInstance().logout();
        LZDeviceService.getInstance().destroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bleReceiveCallback != null) {
            LZDeviceService.getInstance().removeDeviceConnectStateCallback(bleReceiveCallback.getConnectStateListener());
            LZDeviceService.getInstance().removeMeasureDataCallback(bleReceiveCallback.getReceiveListener());
        }
    }
}
