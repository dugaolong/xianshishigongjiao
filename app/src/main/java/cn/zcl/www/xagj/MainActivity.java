package cn.zcl.www.xagj;


import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;


/**
 * Created by dugaolong on 17/3/13.
 */

public class MainActivity extends BaseActivity {
//        implements ActivityCompat.OnRequestPermissionsResultCallback{


    private Context mContext;
    private com.tencent.smtt.sdk.WebView webView;//系统自带的WebView
    private String url = "https://www.xajtfb.cn/BusPage/bus_realtime";
    LinearLayout ll_tencent;
    private ViewGroup mContainer;
    private static final String TAG = "MainActivity";
    //以下的POSITION_ID 需要使用您申请的值替换下面内容
//    private static final String POSITION_ID = "bcaa805adf045251f7bc7f815d0874b5";
//    private static final String POSITION_ID = "23d0920bcc08db5c1d5bc40a66993b0c";
//    private static final String POSITION_ID = "be5546ea85bd8f879bebcf1aaea0c401";
//    private static final String POSITION_ID = "6d3d1a7b95d5be2bc8171fc05231ccf2";//1306
//    private static final String POSITION_ID = "4b485fd9e3e27549417817e03e531a43";//256
//    private static final String POSITION_ID = "21a12924fb79381ad51b842a11c86b2d";//3108

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.webview_layout);
        super.hideTitle(0);


        getWindow().setFormat(PixelFormat.TRANSLUCENT);//（这个对宿主没什么影响，建议声明）
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();

    }

    private void initView() {
        ll_tencent = (LinearLayout) findViewById(R.id.ll_tencent);
        webView = (com.tencent.smtt.sdk.WebView) findViewById(R.id.webview);
        webView.loadUrl(url);
        com.tencent.smtt.sdk.WebSettings webSettings = webView.getSettings();
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
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
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


//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//            // 捕获back键，在展示广告期间按back键，不跳过广告
//            if (mContainer.getVisibility() == View.VISIBLE) {
//                return true;
//            }
//        }
//        return super.dispatchKeyEvent(event);
//    }

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
