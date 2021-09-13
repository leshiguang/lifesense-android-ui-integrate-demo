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
        config.setAppKey("lxad5ccd943ce4d427");
        config.setAppSecret("247d047e1bf98d518b58264997609a708e9ab1e4");
        config.setTn("zhongyizhijia");
        config.setOnline(true);
        config.setDebug(true);
        //替换成自己申请的微信appId和secretKey
        ShareConfig shareConfig = new ShareConfig.Builder().wx("wx123456","123456").build();
        config.setShareConfig(shareConfig);
        LZHealth.getInstance().init(this,config);
    }
}
