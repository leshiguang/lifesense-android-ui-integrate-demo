package com.lifesense.android.demo;

import android.app.Application;

import com.lifesense.android.health.service.Config;
import com.lifesense.android.health.service.LZHealth;
import com.lifesense.share.ShareConfig;

/**
 * Create by qwerty
 * Create on 2021/1/29
 **/
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Config config = new Config();
        config.setAppKey("lx123456");
        config.setAppSecret("234567");
        config.setTn("tangchen");
        //替换成自己申请的微信appid
        ShareConfig shareConfig = new ShareConfig.Builder().wx("wx123456","123456").build();
        config.setShareConfig(shareConfig);
        LZHealth.getInstance().init(this,config);
    }
}
