package com.lifesense.android.demo;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.widget.ImageView;

import com.lifesense.android.health.service.LZHealth;
import com.lifesense.weidong.lswebview.share.ShareApp;
import com.lifesense.weidong.lswebview.share.ShareConfig;
import com.lifesense.weidong.lswebview.share.imgLoader.LoadImgInterface;

import com.lifesense.utils.ImageUtil;
import com.lifesense.utils.LanguageUtil;
import com.lifesense.weidong.lswebview.util.WechatUtils;
import com.lifesense.weidong.lzsimplenetlibs.common.ApplicationHolder;
public class LZApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LZHealth.getInstance().init(this);
        ApplicationHolder.setmApplication(this);
        ImageUtil.init(this);
        LanguageUtil.initLanguage(this);
        ShareConfig config =  new ShareConfig.Builder(this)
                .wb("4085940331")
                .qq("1105910944")
                .fb("1234")
                .twitter("1234","1234")
                .wx(WechatUtils.getWxAppId(this),WechatUtils.getWxAppSecret(this),true)
                .debug(true)
                .imgLoader(new LoadImgInterface() {
                    @Override
                    public void loadImage(ImageView view, String url) {
                        ImageUtil.load(url,view);
                    }
                })
                .shareSuccessIfStay(true)
                .build();
        ShareApp.getInstance().init(this,config);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageUtil.setLocale(base));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LanguageUtil.setLocale(this);
    }
}
