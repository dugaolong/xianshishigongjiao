package com.dgl.www.xagj;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.meizu.advertise.api.SplashAd;
import com.meizu.advertise.api.SplashAdListener;

/**
 * Created by dugaolong on 17/8/10.
 */

public class SplashActivity extends BaseActivity {

    private FrameLayout mContainer;
    private SplashAd mSplashAd;
    private String mzid = "700924774085";
    private static final String TAG = "SplashActivity";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        context = this;

        super.hideTitle(0);
        mContainer = (FrameLayout) findViewById(R.id.container);
        mSplashAd = new SplashAd(this, mzid, new SplashAdListener() {
            @Override
            public void onLoadFinished() {
                mContainer.addView(mSplashAd);
            }

            @Override
            public void onNoAd(long code) {
                Log.d(TAG, "no ad: " + code);
            }

            @Override
            public void onError(String msg) {
                Log.d(TAG, "error: " + msg);
            }

            @Override
            public void onExposure() {
                Log.d(TAG, "onExposure");
            }

            @Override
            public void onClick() {
                Log.d(TAG, "onClick");
            }

            @Override
            public void onAdDismissed() {
                // 跳转至下一个界面
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
