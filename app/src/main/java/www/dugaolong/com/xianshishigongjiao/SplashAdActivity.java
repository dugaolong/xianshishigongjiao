package www.dugaolong.com.xianshishigongjiao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import com.xiaomi.ad.SplashAdListener;
import com.xiaomi.ad.adView.SplashAd;


/**
 * 您可以参考本类中的代码来接入小米游戏广告SDK中的开屏广告。在接入过程中，有如下事项需要注意：
 * 1.请将POSITION_ID值替换成您在小米开发者网站上申请的插播广告位。
 */
public class SplashAdActivity extends Activity {
    private static final String TAG = "SplashAdActivity";
    //以下的POSITION_ID 需要使用您申请的值替换下面内容
    private static final String POSITION_ID = "bcaa805adf045251f7bc7f815d0874b5";
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashad);
        ViewGroup container = (ViewGroup) findViewById(R.id.splash_ad_container);
        SplashAd splashAd = new SplashAd(this, container, new SplashAdListener() {
            @Override
            public void onAdPresent() {
                // 开屏广告展示
                Log.d(TAG, "onAdPresent");
                mHandler.postDelayed(mRemoveDefaultPicture, 500);
            }

            @Override
            public void onAdClick() {
                //用户点击了开屏广告
                Log.d(TAG, "onAdClick");
                gotoNextActivity();
            }

            @Override
            public void onAdDismissed() {
                //这个方法被调用时，表示从开屏广告消失。
                Log.d(TAG, "onAdDismissed");
                gotoNextActivity();
            }

            @Override
            public void onAdFailed(String s) {
                Log.d(TAG, "onAdFailed, message: " + s);
                //这个方法被调用时，表示从服务器端请求开屏广告时，出现错误。
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gotoNextActivity();
                    }
                }, 500);
            }
        });
        splashAd.requestAd(POSITION_ID);
    }

    private Runnable mRemoveDefaultPicture = new Runnable() {
        @Override
        public void run() {
            View logo = findViewById(R.id.splash_default_picture);
            AlphaAnimation alpha = new AlphaAnimation(1, 0);
            alpha.setDuration(200);
            logo.startAnimation(alpha);
            logo.setVisibility(View.GONE);
        }
    };

    /**
     * 跳转到Main Activity
     */
    private void gotoNextActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            // 捕获back键，在展示广告期间按back键，不跳过广告
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}