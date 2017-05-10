package www.dgl.com.xagj;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import net.youmi.android.AdManager;
import net.youmi.android.nm.sp.SplashViewSettings;
import net.youmi.android.nm.sp.SpotListener;
import net.youmi.android.nm.sp.SpotManager;

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

        //有米广告初始化
        AdManager.getInstance(mContext).init("55747da8a977b7eb", "2bc9a7d896756b7e", true);
        SplashViewSettings splashViewSettings = new SplashViewSettings();//实例化开屏广告设置类¶
        splashViewSettings.setAutoJumpToTargetWhenShowFailed(true);//设置展示失败是否自动跳转至设定的窗口¶
        splashViewSettings.setTargetClass(MainActivity.class);//设置开屏结束后跳转的窗口
        //设置开屏控件容器
        // 使用默认布局参数
        splashViewSettings.setSplashViewContainer(mContainer);
        //展示效果
        SpotManager.getInstance(mContext).showSplash(mContext,
                splashViewSettings, new SpotListener() {
                    @Override
                    public void onShowSuccess() {
                        Log.e(TAG, "onShowSuccess");
                    }

                    @Override
                    public void onShowFailed(int i) {
                        Log.e(TAG, "onShowFailed");
                    }

                    @Override
                    public void onSpotClosed() {
                        Log.e(TAG, "onSpotClosed");
                    }

                    @Override
                    public void onSpotClicked(boolean b) {
                        Log.e(TAG, "onSpotClicked");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 开屏展示界面的 onDestroy() 回调方法中调用
        SpotManager.getInstance(mContext).onDestroy();
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
