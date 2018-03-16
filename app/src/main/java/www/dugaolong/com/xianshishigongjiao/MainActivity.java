package www.dugaolong.com.xianshishigongjiao;


import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by dugaolong on 17/3/13.
 */



public class MainActivity extends BaseActivity {

    private Context mContext;
    com.tencent.smtt.sdk.WebView webView;//腾讯X5WebView
    private String url = "https://www.xajtfb.cn/BusPage/bus_realtime";
    Runnable runnableClose=null;
    LinearLayout ll_tencent;
    Dialog dialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tencent_x5);
        mContext = this;
        super.hideTitle(0);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);//（这个对宿主没什么影响，建议声明）
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
    }

    private void initView() {
        ll_tencent = (LinearLayout) findViewById(R.id.ll_tencent);
        webView = (com.tencent.smtt.sdk.WebView)findViewById(R.id.tbsContent);
        webView.loadUrl(url);
        com.tencent.smtt.sdk.WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSavePassword(true);
        webSettings.setGeolocationEnabled(true);
        // 修改ua使得web端正确判断
        String ua = webSettings.getUserAgentString();
        webSettings.setUserAgentString(ua+" MicroMessenger/6.6.5.1280(0x26060532)");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
//            webView.goBack();// 返回前一个页面
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
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