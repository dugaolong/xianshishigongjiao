package com.dgl.www.xagj;

import android.app.Application;
import android.content.Context;

import com.meizu.advertise.api.AdManager;

/**
 * 系统组件
 */
public class MyApplication extends Application {
    private static Context appContext;
    public static MyApplication instance;
    private static final String TAG = "MyApplication";

    //在ssp管理后台(ssp.flyme.cn)申请的应用ID,一个应用对应一个appId
    private static final String APP_ID = "25667347413140";

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        instance = this;

        AdManager.get().init(this, APP_ID); // 初始化广告SDK
    }

    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }

    public static Context getAppContext() {
        return appContext;
    }



}
