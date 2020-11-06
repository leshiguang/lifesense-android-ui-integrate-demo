package com.lifesense.android.demo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;


import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.alibaba.fastjson.JSON;


import com.lifesense.android.bluetooth.core.OnSettingCallBack;
import com.lifesense.android.bluetooth.core.bean.BaseDeviceData;
import com.lifesense.android.bluetooth.core.bean.WifiInfo;
import com.lifesense.android.bluetooth.core.bean.constant.DeviceConnectState;
import com.lifesense.android.bluetooth.core.bean.constant.OperateType;
import com.lifesense.android.bluetooth.core.enums.DataType;
import com.lifesense.android.bluetooth.core.enums.WifiState;
import com.lifesense.android.bluetooth.core.infrastructure.entity.Device;
import com.lifesense.android.bluetooth.pedometer.bean.settings.PedometerCallReminderInfo;
import com.lifesense.android.bluetooth.pedometer.bean.settings.PedometerDialPeaceInfo;
import com.lifesense.android.bluetooth.pedometer.bean.settings.PedometerMessageRemind;
import com.lifesense.android.bluetooth.pedometer.constants.VibrationMode;
import com.lifesense.android.health.service.pages.PageDispatcher;

import com.lifesense.device.scale.application.device.dto.device.FirmwareInfo;
import com.lifesense.device.scale.application.device.dto.receive.BpRecord;
import com.lifesense.device.scale.application.device.dto.receive.WeightData;
import com.lifesense.device.scale.application.interfaces.ILZDeviceService;
import com.lifesense.device.scale.application.interfaces.callback.BleReceiveCallback;

import com.lifesense.device.scale.application.interfaces.listener.OnDataReceiveListener;
import com.lifesense.device.scale.application.interfaces.listener.OnDeviceConnectStateListener;
import com.lifesense.device.scale.application.service.LZDeviceService;

//import com.lifesense.device.scale.device.dto.device.FirmwareInfo;
//import com.lifesense.device.scale.device.dto.receive.WeightData;
//import com.lifesense.device.scale.infrastructure.entity.Device;
import com.lifesense.device.scale.login.LoginResponse;
import com.lifesense.weidong.lswebview.webview.LSWebViewManager;
import com.lifesense.weidong.lswebview.util.ToastUtil;


import com.lifesense.android.health.service.common.BaseActivity;

import com.lifesense.android.health.service.util.PickUtil;
import com.lifesense.weidong.lswebviewmoudle.R;
import com.lifesense.weidong.lzsimplenetlibs.base.BaseResponse;
import com.lifesense.weidong.lzsimplenetlibs.common.ApplicationHolder;
import com.lifesense.weidong.lzsimplenetlibs.cookie.LZCookieManager;
import com.lifesense.weidong.lzsimplenetlibs.net.callback.IRequestCallBack;
import com.lifesense.weidong.lzsimplenetlibs.net.dispatcher.DefaultApiDispatcher;
import com.lifesense.weidong.lzsimplenetlibs.util.RequestCommonParamsUtils;

import java.util.List;


public class MainActivity extends BaseActivity {

    private static final String domain = "https://api-r1.leshiguang.com";
    private TextView tvWeight;
    private Button btDeviceList;

    private com.lifesense.android.bluetooth.core.bean.WifiInfo cache = new com.lifesense.android.bluetooth.core.bean.WifiInfo();
    private String mac;

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
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){//未开启定位权限
            //开启定位权限,200是标识码
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},200);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){//未开启定位权限
            //开启定位权限,200是标识码
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},200);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED){//未开启定位权限
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.BLUETOOTH},200);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_ADMIN)
                != PackageManager.PERMISSION_GRANTED){//未开启定位权限
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.BLUETOOTH_ADMIN},200);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_PRIVILEGED)
                != PackageManager.PERMISSION_GRANTED){//未开启定位权限
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.BLUETOOTH_PRIVILEGED},200);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){//未开启定位权限
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.BLUETOOTH_PRIVILEGED},200);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){//未开启定位权限
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.BLUETOOTH_PRIVILEGED},200);
        }

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED){//未开启定位权限
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_SMS},200);
        }

        setContentView(R.layout.activity_main);
        tvWeight = findViewById(R.id.tv_weight);
        btDeviceList = findViewById(R.id.toDeviceList);
        btDeviceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(!isLogin()) {
//                    return;
//                }
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
                ToastUtil.showCustomCenterShowToast(MainActivity.this,"已退出登录!");
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
        if(requestCode == 1001) {
            if(checkPermission()) {
                login();
            } else {
                ToastUtil.showSingletonToast(MainActivity.this,"缺少权限!");
            }
        }
    }

    private boolean checkPermission() {
        return checkReadPermission(new String[] {Manifest.permission.BLUETOOTH,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION},1001);
    }

    private void login() {
        String ANDROID_ID = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        //静默登录
        BleReceiveCallback bleReceiveCallback = new BleReceiveCallback();
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
                        Log.i("MainAct", "check >>>"+ deviceConnectState1.name());

                        tvWeight.append("onDeviceConnectStateChange==>"+deviceConnectState.name());
//                        PedometerCallReminderInfo pedometerCallReminderInfo = new PedometerCallReminderInfo();
//                        pedometerCallReminderInfo.setEnable(true);
//                        pedometerCallReminderInfo.setRemindType(0x01);
//                        pedometerCallReminderInfo.setEnableRemind(true);
//                        pedometerCallReminderInfo.setVibrationDelay(30);
//                        pedometerCallReminderInfo.setVibrationIntensity1(5);
//                        pedometerCallReminderInfo.setVibrationIntensity2(5);
//                        pedometerCallReminderInfo.setVibrationMode(VibrationMode.CONTINUOUS_VIBRATION);
//
//                        LZDeviceService.getInstance().setDeviceSettings(s, pedometerCallReminderInfo, OperateType.UPDATE, new OnSettingCallBack() {
//                            @Override
//                            public void onSuccess(String macAddress) {
//                                Log.i("MainAct", "setting SCUUESS");
//                            }
//                        });
                        if(deviceConnectState1 == DeviceConnectState.CONNECTED_SUCCESS) {
                            MainActivity.this.mac = s;
                        }
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


        bleReceiveCallback.setReceiveListener(new OnDataReceiveListener() {
            @Override
            public void onReceiveDeviceMeasureData(DataType dataType, BaseDeviceData baseDeviceData) {
                Log.i("MainAct", JSON.toJSONString(baseDeviceData));
            }

            @Override
            public void onReceiveWeightData(WeightData weightData) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvWeight.setText(JSON.toJSONString(weightData));
                    }
                });
            }

            @Override
            public void onReceiveBpMeasureInfo(BpRecord bpRecord) {
//                tvWeight.setText(JSON.toJSONString(bpRecord));
            }

        });
        LZDeviceService.getInstance().login(getApplicationContext(), "appkey", "appSecret", ANDROID_ID, new IRequestCallBack<BaseResponse>() {
            @Override
            public void onRequestSuccess(BaseResponse response) {
                LoginResponse loginResponse = (LoginResponse) response;

                LZDeviceService.getInstance().init(MainActivity.this, bleReceiveCallback);
                RequestCommonParamsUtils.put("appVersion", "4.6");

                LZDeviceService.getInstance().syncDeviceFromService(new ILZDeviceService.OnSyncDeviceCallback() {
                    @Override
                    public void onSyncDeviceSuccess(List<Device> list) {
                        LZDeviceService.getInstance().startDataReceive();
                    }

                    @Override
                    public void onSyncDeviceFailed(int i, String s) {

                    }
                });
                //初始化cookie
                LSWebViewManager.getInstance().init(loginResponse.getLoginEntity().getUserId(), loginResponse.getLoginEntity().getAccessToken());
                LSWebViewManager.getInstance().setDebug(true);

                //设置用户信息
//                User user = UserManager.getInstance(MainActivity.this).getUser();
//                user.setWeight(73);
//                user.setBirthday(new Date());
//                user.setName("无用");
//                DeviceNetManager.getInstance().updateUser(user, null);
                ToastUtil.showCustomCenterShowToast(MainActivity.this,"登陆成功!");


            }

            @Override
            public void onRequestError(int i, String s, BaseResponse response) {
                Log.e("login---------", "msg : " + s + ", code : " + i);
            }
        });

    }


    private void choiceUpgradeFile() {
        ToastUtil.showCustomCenterShowToast(MainActivity.this,"请要升级的选择固件文件");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//无类型限制
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 2);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String path = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                path = PickUtil.getPath(this, uri);
            }
            ToastUtil.showCustomCenterShowToast(MainActivity.this,"选择的文件是:" + path);
            Device connectedDevice = getConnectedDevice();
            if (connectedDevice == null) {
                ToastUtil.showCustomCenterShowToast(MainActivity.this,"未绑定设备");
                return;
            }
            FirmwareInfo info = new FirmwareInfo();
            info.setFileName(path);
//            startActivity(TestDeviceUpgradeActivity.makeIntent(this, connectedDevice.getId(), info));
        }
    }

    private Device getConnectedDevice() {
        Device connectedDevice = null;
        List<Device> deviceList = LZDeviceService.getInstance().getBondedDevices();
        if (deviceList != null) {
            for (Device device : deviceList) {
                if (DeviceConnectState.CONNECTED_SUCCESS == LZDeviceService.getInstance().checkDeviceConnectState(device.getMac())) {
                    connectedDevice = device;
                }
            }
        }
        return connectedDevice;
    }

//    public boolean isLogin() {
//        return LZDeviceService.getInstance().getCurrentUser() != null;
//    }

    public void logout() {
        LZCookieManager.getInstance().clearCookie();
        LSWebViewManager.getInstance().destroy();
        LZDeviceService.getInstance().logout();
        LZDeviceService.getInstance().destroy();
    }

    private void testSetting() {
//        PedometerSceenContent pedometerSceenContent= new PedometerSceenContent();
//        List<PedometerSceenContent.PedometerPage> pedometerPages = new ArrayList<>();
//        pedometerPages.add(PedometerSceenContent.PedometerPage.TIME);//时间
//        pedometerPages.add(PedometerSceenContent.PedometerPage.CALORIE);//心率
//        pedometerPages.add(PedometerSceenContent.PedometerPage.STEP);//步数
////        pedometerPages.add(PedometerSceenContent.PedometerPage.HEARTRATE);
////        pedometerPages.add(PedometerSceenContent.PedometerPage.DISTANCE);
////
////        pedometerPages.add(PedometerSceenContent.PedometerPage.RUNNING);//跑步
////        pedometerPages.add(PedometerSceenContent.PedometerPage.WALKING);//健走
////        pedometerPages.add(PedometerSceenContent.PedometerPage.WEATHER);//天气
////        pedometerPages.add(PedometerSceenContent.PedometerPage.CYCLING);//骑行
////        pedometerPages.add(PedometerSceenContent.PedometerPage.BATTERY);//电量
////        pedometerPages.add(PedometerSceenContent.PedometerPage.CALORIE);//卡路里
////        pedometerPages.add(PedometerSceenContent.PedometerPage.AliPay);//支付宝
//        pedometerSceenContent.setPedometerPages(pedometerPages);
        PedometerDialPeaceInfo pedometerDialPeaceInfo = new PedometerDialPeaceInfo();
        pedometerDialPeaceInfo.setDialPeace(PedometerDialPeaceInfo.DialPeaceStyle.DialPeace3);
        PedometerCallReminderInfo pedometerCallReminderInfo = new PedometerCallReminderInfo();
        pedometerCallReminderInfo.setEnable(true);
        pedometerCallReminderInfo.setEnableRemind(true);
        pedometerCallReminderInfo.setRemindType(1);
        pedometerCallReminderInfo.setVibrationDelay(1000);
        pedometerCallReminderInfo.setVibrationIntensity1(1);
        pedometerCallReminderInfo.setVibrationMode(VibrationMode.INTERMITTENT_VIBRATION1);

        PedometerMessageRemind pedometerMessageRemind = new PedometerMessageRemind();
        pedometerMessageRemind.setMessageType(PedometerMessageRemind.MessageType.QQ);
        pedometerMessageRemind.setEnable(true);
        List pedometerEventReminders = LZDeviceService.getInstance().getDeviceSettings(mac, PedometerCallReminderInfo.class);
        System.out.println();
        LZDeviceService.getInstance().setDeviceSettings(mac, pedometerMessageRemind, OperateType.ADD, new OnSettingCallBack() {
            @Override
            public void onSuccess(String macAddress) {
                super.onSuccess(macAddress);
                System.out.println("设置成功");

            }

            @Override
            public void onFailure(int errorCode) {
                super.onFailure(errorCode);
                System.out.println("设置失败" + errorCode);
            }

            @Override
            public void onConfigInfo(Object obj) {
                super.onConfigInfo(obj);
            }
        });
    }
}
