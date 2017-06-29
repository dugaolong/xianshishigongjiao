package www.dugaolong.com.xianshishigongjiao;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qq.e.ads.splash.SplashAD;


/**
 * Created by dugaolong on 17/3/13.
 */

public class MainActivity extends BaseActivity {
    private Context mContext;
    private WebView webView;//系统自带的WebView
    private String url = "http://www.xaglkp.com.cn/BusPage/bus_realtime?from=groupmessage&isappinstalled=0";
    LinearLayout ll_tencent;
    private static final String TAG = "MainActivity";
    //以下的POSITION_ID 需要使用您申请的值替换下面内容
    private static final String POSITION_ID = "4b485fd9e3e27549417817e03e531a43";//256
    public boolean canJump = false;
    private static final String SKIP_TEXT = "点击跳过 %d";
    private SplashAD splashAD;
    private ViewGroup container;
    private TextView skipView;
    private ImageView splashHolder;
    public static final String APPID = "1101152570";
    public static final String SplashPosID = "8863364436303842593";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.webview_layout);
        super.hideTitle(0);

        skipView = (TextView) findViewById(R.id.skip_view);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);//（这个对宿主没什么影响，建议声明）
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();

        container = (ViewGroup) findViewById(R.id.splash_container);
        splashHolder = (ImageView) findViewById(R.id.splash_holder);

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
    protected void findWidgets() {

    }

    @Override
    protected void initComponent() {

    }

    @Override
    protected void getIntentData() {

    }

}
