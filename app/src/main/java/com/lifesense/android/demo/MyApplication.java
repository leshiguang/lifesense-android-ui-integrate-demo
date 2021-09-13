package com.lifesense.android.demo;

import android.app.Application;

import com.lifesense.android.health.service.Config;
import com.lifesense.android.health.service.LZHealth;
import com.lifesense.share.ShareConfig;
import com.lifesense.weidong.lzsimplenetlibs.net.dispatcher.DefaultApiDispatcher;

/**
 * Create by qwerty
 * Create on 2021/1/29
 **/
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Config config = new Config();
        config.setAppKey("your appkey");
        config.setAppSecret("your appsecret");
        config.setTn("your tn");
        config.setOnline(true);
        config.setDebug(true);
        //替换成自己申请的微信appId和secretKey
        ShareConfig shareConfig = new ShareConfig.Builder().wx("wx123456","123456").build();
        config.setShareConfig(shareConfig);
        LZHealth.getInstance().init(this,config);
    }
}
