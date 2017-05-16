package com.dgl.www.xagj;

import android.os.Bundle;
import android.view.ViewGroup;

/**
 * Created by dugaolong on 17/5/10.
 */

public class SplashActivity extends BaseActivity {

    private static final String TAG = "SplashActivity";
    private ViewGroup mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        super.hideTitle();
        mContainer = (ViewGroup) findViewById(R.id.splash_ad_container);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 开屏展示界面的 onDestroy() 回调方法中调用
//        SpotManager.getInstance(mContext).onDestroy();
    }

    @Override
    protected void findWidgets() {

    }

    @Override
    protected void initComponent() {

    }

    @Override
    protected void getIntentData() {

    }


}
