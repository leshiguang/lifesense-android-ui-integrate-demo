package com.lifesense.android.demo;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.widget.ImageView;

import com.lifesense.weidong.lswebview.share.ShareApp;
import com.lifesense.weidong.lswebview.share.ShareConfig;
import com.lifesense.weidong.lswebview.share.imgLoader.LoadImgInterface;

import com.lifesense.utils.ImageUtil;
import com.lifesense.utils.LanguageUtil;
import com.lifesense.weidong.lswebview.util.WechatUtils;
import com.lifesense.weidong.lzsimplenetlibs.common.ApplicationHolder;
import com.lifesense.weidong.lzsimplenetlibs.util.PreferencesUtils;
import com.tencent.tauth.AuthActivity;

import java.util.ArrayList;
import java.util.List;

public class LZApplication extends Application {



    @Override
    public void onCreate() {
        super.onCreate();


        ApplicationHolder.setmApplication(this);
        changeDomain();
        ImageUtil.init(this);
        LanguageUtil.initLanguage(this);
        ActivityInfo info = null;
        try {
            ComponentName cn = new ComponentName(this, AuthActivity.class);
            info = getPackageManager().getActivityInfo(cn, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
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

    private List<Long> getAllUsers(){
        List<Long> users = new ArrayList<>();
        String userId = PreferencesUtils.getString(this,"userId","0");
        users.add(Long.parseLong(userId));
        return users;
    }

    private void initShare(Application application) {
        ActivityInfo info = null;
        try {
            ComponentName cn = new ComponentName(this, AuthActivity.class);
            info = getPackageManager().getActivityInfo(cn, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String QQID = info.metaData.getString("QQ_APP").replace("tencent", "");

        ShareConfig config =  new ShareConfig.Builder(application)
                .wb("4085940331")
                .qq(QQID)
                .wx(WechatUtils.getWxAppId(this), WechatUtils.getWxAppSecret(this),true)
                .debug(true)
                .imgLoader(new LoadImgInterface() {
                    @Override
                    public void loadImage(ImageView view, String url) {
                        ImageUtil.load(url,view);
                    }
                })
                .shareSuccessIfStay(true)
                .build();

        ShareApp.getInstance().init(application,config);
    }

    /**
     * 切换APP环境
     */
    private void changeDomain() {
//        SharedPreferencesConfig config = SharedPreferencesConfig.getInstance();
//        String path = config.getString("CONFIG_PATH", "config/Default.xml");
//
//        Log.i("TIM", "===LifesenseApplication  changeDomain   path===" + path);
//
//        String domain = config.getString("CONFIG_DOMAIN", null);
//        if (null != Config.CONFIG_XML_PATH && !StringUtil.isEmptyOrNullChar(path)) {
//            Config.CONFIG_XML_PATH = path;
//        } else {
//            Config.CONFIG_XML_PATH = "config/Default.xml";
//        }
//        if (null != domain && !StringUtil.isEmptyOrNullChar(domain)) {
//            Config.CONFIG_HOST = domain;
//            Config.CONFIG_DOMIAN_PATH = "https://" + domain;
//            Config.ISOTHERDOMAIN = true;
//        } else {
//            Config.CONFIG_HOST = "";
//            Config.CONFIG_DOMIAN_PATH = "";
//            Config.ISOTHERDOMAIN = false;
//        }
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
