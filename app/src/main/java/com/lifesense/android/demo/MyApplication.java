package com.lifesense.android.demo;

import android.app.Application;

import com.lifesense.android.health.service.Config;
import com.lifesense.android.health.service.LZHealth;

/**
 * Create by qwerty
 * Create on 2021/1/29
 **/
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Config config = new Config();
        config.setAppKey("lx66f7cab36fdd119c");
        config.setAppSecret("d5836b5c336b840103227e2d0777a0911806f204");
        config.setTn("dingding");
        config.setDebug(true);
        LZHealth.getInstance().init(this,config);
    }
}
