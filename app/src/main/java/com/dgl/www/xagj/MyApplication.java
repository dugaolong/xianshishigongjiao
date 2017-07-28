package com.dgl.www.xagj;

import android.app.Application;
import android.content.Context;

/**
 * 系统组件
 */
public class MyApplication extends Application {
    private static Context appContext;
    public static MyApplication instance;
    private static final String TAG = "MyApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        instance = this;

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


    public String getRunningActivityName() {
        android.app.ActivityManager mActivityManager = (android.app.ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        return mActivityManager.getRunningTasks(1).get(0).topActivity.getClassName();
    }

}
