package com.dgl.www.xagj;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.jfpush.JManager;

/**
 * Created by dugaolong on 17/3/13.
 */

public class MainActivity extends BaseActivity
{
//        implements ActivityCompat.OnRequestPermissionsResultCallback{

    public static final int INT_ACCESS_FINE_LOCATION = 1;
    private Context mContext;
    private WebView webView;//系统自带的WebView
    private String url = "http://www.xaglkp.com.cn/BusPage/bus_realtime?from=groupmessage&isappinstalled=0";
    LinearLayout ll_tencent;
    private static final String TAG = "MainActivity";

    /**
     * TODO Demo中使用测试Key，仅用于调试；发布时，必须替换为开发者在平台申请的正式Key，否则无收入
     */
    private static final String KEY_JUFU = "604f51b4deb74d2f4ffb71c8ec7f9f44";

    /**
     * 官方提供的默认渠道号，可自定义 official
     */
    private static final String CHANNEL = "xiaomi";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.webview_layout);
        super.hideTitle();


        getWindow().setFormat(PixelFormat.TRANSLUCENT);//（这个对宿主没什么影响，建议声明）
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();


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

        // 获取PManager实例
        JManager pManager = JManager.getInstance(this, KEY_JUFU, CHANNEL);

        // 设置自定义通知栏布局文件ID
        pManager.setResId(R.layout.f_custom_noti, R.id.noti_icon, R.id.noti_title, R.id.noti_time,
                R.id.noti_content);

        // 接收JfPush 的广告消息 {注：true 为自动模式（定时策略和事件触发的自动获取），
        // false 为手动模式（每调用一次，获取一次广告）
        pManager.getMessage(this, true);
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
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return true;
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
