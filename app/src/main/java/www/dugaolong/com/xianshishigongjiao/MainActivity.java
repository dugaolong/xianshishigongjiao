package www.dugaolong.com.xianshishigongjiao;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.widget.LinearLayout;

import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by dugaolong on 17/3/13.
 */



public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private Context mContext;
    com.tencent.smtt.sdk.WebView webView;//腾讯X5WebView
    private String url = "https://www.xajtfb.cn/BusPage/bus_realtime";
    Runnable runnableClose=null;
    LinearLayout ll_tencent;
    Dialog dialog ;

    String[] request_perms = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private int RC_CAMERA=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tencent_x5);
        mContext = this;
        super.hideTitle(0);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);//（这个对宿主没什么影响，建议声明）
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        if (EasyPermissions.hasPermissions(this, request_perms)) {
        } else {
            EasyPermissions.requestPermissions(this, "需要获取存储和手机识别码权限",
                    RC_CAMERA, request_perms);
        }
    }

    private void initView() {
        ll_tencent = (LinearLayout) findViewById(R.id.ll_tencent);
        webView = (com.tencent.smtt.sdk.WebView)findViewById(R.id.tbsContent);
//        webView.loadUrl(url);
        com.tencent.smtt.sdk.WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSavePassword(true);
        webSettings.setGeolocationEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setHorizontalScrollbarOverlay(true);
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        //设置定位的数据库路径
        webSettings.setGeolocationDatabasePath(dir);

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

        if (Build.VERSION.SDK_INT >= 23) {
            int checkPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else {
                webView.loadUrl(url, extraHeaders);
            }
        }
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

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        boolean permissionDenied = EasyPermissions.somePermissionDenied(MainActivity.this, perms.toArray(new String[perms.size()]));
        boolean permissionPermanentlyDenied = EasyPermissions.somePermissionPermanentlyDenied(MainActivity.this, perms);
        if (permissionDenied || permissionPermanentlyDenied) {
            int size = perms.size();
            String str_reason = getString(R.string.refused_storage_tip);
            if (size == 1) {
                if (perms.get(0).equals(this.request_perms[1])) {
                    str_reason = getString(R.string.refused_phone_state_tip);
                }
            }
            //在权限弹窗中，用户勾选了'不在提示'且拒绝权限的情况触发，可以进行相关提示。
            new AppSettingsDialog.Builder(this).setRationale(str_reason).setNegativeButton("取消").build().show();
        } else {
            //退出APP
            CrashHandler.getInstance().exitApp();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);


    }
}