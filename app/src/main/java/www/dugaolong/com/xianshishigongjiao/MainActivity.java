package www.dugaolong.com.xianshishigongjiao;


import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import www.dugaolong.com.xianshishigongjiao.utils.DialogTipsUtil;
import www.dugaolong.com.xianshishigongjiao.utils.NetUtil;


/**
 * Created by dugaolong on 17/3/13.
 */

public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
//        implements ActivityCompat.OnRequestPermissionsResultCallback{


    private Context mContext;
    private WebView webView;//系统自带的WebView
//    private String url = "https://www.xajtfb.cn/BusPage/bus_line";
    private String url = "https://www.xajtfb.cn/BusPage/bus_realtime";
//    LinearLayout ll_tencent;
    private ViewGroup mContainer;
    private static final String TAG = "MainActivity";
    //以下的POSITION_ID 需要使用您申请的值替换下面内容
//    private static final String POSITION_ID  "21a12924fb79381ad51b842a11c86b2d";//3108
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mContainer.setVisibility(View.GONE);
        }
    };

    String[] request_perms = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int RC_CAMERA=0;
    private static final int RC_STORAGE_PHONE_STATE = 100;//手机状态,存储
    private MyApplication application;
    private SharedPreferences preference;
    public static final String HAS_SHOW_PRIVACY_POLICY = "privacy_policy";//隐私政策
    private boolean has_show_privacy_policy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.webview_layout);
        super.hideTitle(0);
        preference = PreferenceManager.getDefaultSharedPreferences(this);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);//（这个对宿主没什么影响，建议声明）
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();

        mContainer = (ViewGroup) findViewById(R.id.splash_ad_container);
        mContainer.setBackgroundResource(R.drawable.splash_default_picture);

        requestBasicPermission();
        checkshowPrivate();
//        SplashAd splashAd = new SplashAd(this, mContainer, R.drawable.splash_default_picture, new SplashAdListener() {
//            @Override
//            public void onAdPresent() {
//                // 开屏广告展示
//                Log.d(TAG, "onAdPresent");
//            }
//
//            @Override
//            public void onAdClick() {
//                //用户点击了开屏广告
//                Log.d(TAG, "onAdClick");
//            }
//
//            @Override
//            public void onAdDismissed() {
//                //这个方法被调用时，表示从开屏广告消失。
//                Log.d(TAG, "onAdDismissed");
//            }
//
//            @Override
//            public void onAdFailed(String s) {
//                Log.d(TAG, "onAdFailed, message: " + s);
//            }
//        });
//        splashAd.requestAd(POSITION_ID);
    }
    /*判断是否第一次登陆APP.
     */
    private void checkshowPrivate() {
        has_show_privacy_policy = preference.getBoolean(HAS_SHOW_PRIVACY_POLICY, false);
        if (!has_show_privacy_policy) {
            showPrivatePolicy();
        }
    }

    /**
     * 显示隐私政策
     */
    private void showPrivatePolicy() {
        DialogTipsUtil.showPrivate(2, this, "隐私政策", this.getString(R.string.private_policy_content), "同意", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogTipsUtil.dialog.dismiss();
                //保存已经同意"隐私政策标记
                preference.edit().putBoolean(HAS_SHOW_PRIVACY_POLICY, true).apply();
            }
        }, "不同意", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //退出登录,关掉所有页面
               finishAll();
                //退出APP
                exitApp();
            }
        });
    }

    /**
     * 退出应用程序
     */
    public void exitApp() {
        try {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
        }
    }

    @AfterPermissionGranted(RC_STORAGE_PHONE_STATE)
    private void requestBasicPermission()
    {
        if (EasyPermissions.hasPermissions(this, request_perms)) {
//            initData();
        } else {
            EasyPermissions.requestPermissions(this, "需要获取存储和手机识别码权限",
                    RC_STORAGE_PHONE_STATE, request_perms);
        }
    }
    private void countDown() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        },3000);
    }

    private void initView() {
//        ll_tencent = (LinearLayout) findViewById(ll_tencent);
        webView = (WebView) findViewById(R.id.webview);
//        webView.loadUrl(url);
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
        //HTTP 和 HTTPS 混合调用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        // 修改ua使得web端正确判断
        String ua = webSettings.getUserAgentString();
        webSettings.setUserAgentString(ua+" MicroMessenger/6.6.5.1280(0x26060532)");
        Map extraHeaders = new HashMap();
        extraHeaders.put("Referer", "https://www.xajtfb.cn/Defaultm/Transportation");
        webView.loadUrl(url, extraHeaders);

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
//                view.loadUrl(url);
//                Log.d(TAG, "url==="+url);
                Map extraHeaders = new HashMap();
                extraHeaders.put("Referer", "https://www.xajtfb.cn/Defaultm/Transportation");
                view.loadUrl(url, extraHeaders);
                return true;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//            // 捕获back键，在展示广告期间按back键，不跳过广告
//            if (mContainer.getVisibility() == View.VISIBLE) {
//                return true;
//            }
//        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishAll();
    }


    @Override
    protected void onResume() {
        super.onResume();

        //判断网络是否可用
        if(NetUtil.detectNetStatus(this)){
            //2秒以后图片消失
            countDown();
        }else {
            NetUtil.setNetwork(this);
        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}
