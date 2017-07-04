package www.dugaolong.com.xianshishigongjiao;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.alimama.mobile.sdk.MmuSDK;
import com.alimama.mobile.sdk.config.InsertController;
import com.alimama.mobile.sdk.config.InsertProperties;
import com.alimama.mobile.sdk.config.MmuController;
import com.alimama.mobile.sdk.config.MmuSDKFactory;


/**
 * Created by dugaolong on 17/3/13.
 */

public class MainActivity extends BaseActivity {
    private Context mContext;
    private WebView webView;//系统自带的WebView
    private String url = "http://www.xaglkp.com.cn/BusPage/bus_realtime?from=groupmessage&isappinstalled=0";
    LinearLayout ll_tencent;
    private static final String TAG = "MainActivity";
    private InsertProperties properties;
    private InsertController<?> mController;
    private MmuSDK mmuSDK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.webview_layout);
        super.hideTitle(0);

        getWindow().setFormat(PixelFormat.TRANSLUCENT);//（这个对宿主没什么影响，建议声明）
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();

        ViewGroup nat = (ViewGroup) findViewById(R.id.nat);
        String slotId = "67546";//注意：该广告位只做测试使用，请勿集成到发布版app中
        setupAlimama(nat, slotId);


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
    protected void onDestroy() {
        super.onDestroy();
        finishAll();
    }

    @Override
    public void onBackPressed() {
        boolean interrupt = false;
        if (mController != null) {// 通知Banner推广返回键按下，如果Banner进行了一些UI切换将返回true
            // 否则返回false(如从 expand状态切换会normal状态将返回true)
            interrupt = mController.onBackPressed();
        }

        if (!interrupt)
            super.onBackPressed();
    }

    private void setupAlimama(ViewGroup nat, String slotId) {
        mmuSDK = MmuSDKFactory.getMmuSDK();
        mmuSDK.init(getApplication());//初始化SDK,该方法必须保证在集成代码前调用，可移到程序入口处调用
        mmuSDK.accountServiceInit(this);
        properties = new InsertProperties(slotId, nat);
        mmuSDK.attach(properties);

        properties.setClickCallBackListener(new MmuController.ClickCallBackListener() {

            @Override
            public void onClick() {
                Log.i("munion", "onclick");
            }
        });

        properties.setOnStateChangeCallBackListener(new InsertController.OnStateChangeCallBackListener() {

            @Override
            public void onStateChanged(InsertController.InterstitialState state) {
                Log.i("munion", "state = " + state);
            }
        });
        mController =  properties.getMmuController();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mmuSDK.accountServiceHandleResult(requestCode, resultCode, data,this)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    public void back(View v){
        onBackPressed();
    }

}
