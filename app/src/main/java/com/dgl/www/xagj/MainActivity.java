package com.dgl.www.xagj;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.newjf.spot.newJManager;


/**
 * Created by dugaolong on 17/3/13.
 */

public class MainActivity extends BaseActivity {
//        implements ActivityCompat.OnRequestPermissionsResultCallback{


    public static final String  LOG = "MainActivity";
    public static final int INT_ACCESS_FINE_LOCATION = 1;
    private Context mContext;
    private WebView webView;//系统自带的WebView
    private String url = "http://www.xaglkp.com.cn/BusPage/bus_realtime?from=groupmessage&isappinstalled=0";
    LinearLayout ll_tencent;
    private static final String TAG = "MainActivity";

    /**
     * TODO Demo中使用测试Key，仅用于调试；发布时，必须替换为开发者在平台申请的正式Key，否则无收入
     */
    private static final String KEY_JUFU = "b7f27a11f9fcecc11e819b68ff9938ed";

    /**
     * 官方提供的默认渠道号，可自定义 official
     */
    private static final String CHANNEL = "xiaomi";
    newJManager mJManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.webview_layout);
        super.hideTitle();


        getWindow().setFormat(PixelFormat.TRANSLUCENT);//（这个对宿主没什么影响，建议声明）
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();

        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);

        Log.e(LOG,"IMEI=="+tm.getDeviceId());
        jufuAd();

    }

    //聚富广告
    private void jufuAd() {
        /**
         * <pre>
         *  参数说明：
         * 		JManager.getInstance(Context arg0, String arg1, String arg2)
         * 			arg0 : 上下文，传当前activity即可；
         * 			arg1 : key，开发者在后台查询；
         * 			arg2 : channel
         * </pre>
         */

//        Toast.makeText(MainActivity.this, "receiving...", Toast.LENGTH_LONG).show();

        mJManager = newJManager.newgetInstance(this, KEY_JUFU, CHANNEL, 3);
        mJManager.newshowAds(this);
    }

    private void initView() {
        ll_tencent = (LinearLayout) findViewById(R.id.ll_tencent);
        webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl(url);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSavePassword(true);
        //启用数据库
        webSettings.setDatabaseEnabled(true);
        //设置定位的数据库路径
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        webSettings.setGeolocationDatabasePath(dir);
        //启用地理定位
        webSettings.setGeolocationEnabled(true);
        //开启DomStorage缓存
        webSettings.setDomStorageEnabled(true);
        //配置权限
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 调用之前必须要初始化广告
            if (mJManager != null) {
                mJManager.newshowExitDialog(this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO 释放资源
                        // 注：这里不能用android.os.Process.killProcess(android.os.Process.myPid())退出！否则无法显示退出广告
                    }
                });
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onDestroy() {
        if (mJManager != null) {
            mJManager.newcolseExitDialog();
        }

        super.onDestroy();
        finishAll();
    }


    @Override
    protected void onResume() {
        super.onResume();
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
