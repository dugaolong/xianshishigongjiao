package www.dugaolong.com.xianshishigongjiao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import www.dugaolong.com.xianshishigongjiao.utils.LinkifyUtil;



public class PublicWebActivity extends BaseActivity implements
        OnClickListener {
    private WebView mWebView;
    private TextView titleText;
    private TextView btn_back_close;
    private TextView btn_back_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_title_browser_activity);
        super.hideTitle(0);
        initView();
        Intent intent = getIntent();
        if (intent != null) {
            Uri data = intent.getData();
            String dataurl = data.getQueryParameter("url");
            if (dataurl != null) {
                if (dataurl.replace(LinkifyUtil.PRIVACY_POLICY_CONTENT,"").equals(Const.PRIVACY_POLICY_CMD)) {
                    mWebView.loadUrl(Const.PRIVACY_POLICY_URL);
                }
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        titleText = (TextView) findViewById(R.id.title);
        titleText.setText("隐私政策");
        btn_back_close = (TextView) findViewById(R.id.btn_back_close);
        btn_back_return = (TextView) findViewById(R.id.btn_back_return);
        btn_back_close.setOnClickListener(this);
        btn_back_return.setOnClickListener(this);
        btn_back_return = (TextView) findViewById(R.id.btn_back_return);
        mWebView = (WebView) this.findViewById(R.id.notitle_webview);
        mWebView.setBackgroundColor(0xffecf1f2);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
        webSettings.setAllowFileAccess(true);
//        webSettings.setBuiltInZoomControls(false);
        webSettings.setUseWideViewPort(false);

        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);

        webSettings.setSavePassword(false);
        webSettings.setSupportZoom(false);
        webSettings.setUseWideViewPort(false);
        // 自适应屏幕
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.requestFocus();

        // 提高渲染的优先级
        webSettings.setRenderPriority(RenderPriority.HIGH);
        String appCachePath = mContext.getDir("netCache", Context.MODE_PRIVATE)
                .getAbsolutePath();
//        webSettings.setAppCacheEnabled(true);
//        webSettings.setAppCachePath(appCachePath);
//        webSettings.setAppCacheMaxSize(1024 * 1024 * 5);
//        webSettings.setDatabaseEnabled(true);
//        webSettings.setDomStorageEnabled(true);
//        String databasePath = mContext
//                .getDir("databases", Context.MODE_PRIVATE).getPath();
//        webSettings.setDatabasePath(databasePath);

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

//        mWebView.setWebChromeClient(new MyWebChromeClient());
    }


//    private class MyWebChromeClient extends WebChromeClient {
//        @Override
//        public void onReceivedTitle(WebView view, String title) {
//            super.onReceivedTitle(view, title);
//            if (!TextUtils.isEmpty(title)) {
//                PublicWebActivity.this.titleText.setText(title.length() > 8 ? title.substring(0, 6) + "..." : title);
//            }
//
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.freeMemory();
        mWebView.destroy();
        mWebView = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onClick(View arg0) {
        int id = arg0.getId();
        if (id == R.id.btn_back_close) {
            finish();
        } else if (id == R.id.btn_back_return) {
            onBackBtClick();
        }
    }

    /**
     * 返回键通用回调处理，能GoBack则goBack，否则finish
     *
     * @return 如果是系统返回键调用，true表示消化按键事件，false则相反
     */
    private boolean onBackBtClick() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();// 返回前一个页面
            return true;
        } else {
            finish();
        }
        return false;
    }

}