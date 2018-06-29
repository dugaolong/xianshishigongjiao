package www.dugaolong.com.xianshishigongjiao;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * Created by dugaolong on 17/3/13.
 */

public class MainActivity extends BaseActivity {
//        implements ActivityCompat.OnRequestPermissionsResultCallback{


    private Context mContext;
    private WebView webView;//系统自带的WebView
//    private String url = "http://www.xaglkp.com.cn/BusPage/bus_realtime?from=groupmessage&isappinstalled=0";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.webview_layout);
        super.hideTitle(0);

        getWindow().setFormat(PixelFormat.TRANSLUCENT);//（这个对宿主没什么影响，建议声明）
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();

        mContainer = (ViewGroup) findViewById(R.id.splash_ad_container);
        mContainer.setBackgroundResource(R.drawable.splash_default_picture);
        //5秒以后图片消失
        countDown();

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

    private void countDown() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        },2000);
    }

    private void initView() {
//        ll_tencent = (LinearLayout) findViewById(ll_tencent);
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
        // 修改ua使得web端正确判断
        String ua = webSettings.getUserAgentString();
        webSettings.setUserAgentString(ua+" MicroMessenger/6.6.5.1280(0x26060532)");
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
