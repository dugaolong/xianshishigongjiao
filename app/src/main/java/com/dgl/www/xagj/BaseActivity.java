package com.dgl.www.xagj;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.youmi.android.nm.sp.SpotManager;

import static com.dgl.www.xagj.MainActivity.INT_ACCESS_FINE_LOCATION;


/**
 * activity基类
 */
public abstract class BaseActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback{

    public static Activity instance;
    private String session;
    public static final String formatter = "%s_%s_%s".replaceAll("_", "::");

    public Context mContext;
    protected DisplayMetrics metric;
    protected int screenWidth;
    protected int screenHeight;

    /**
     * 初始化控件
     */
    protected abstract void findWidgets();

    /**
     * 初始化控件数据
     */
    protected abstract void initComponent();

    /**
     * 初始化数据
     */
    protected abstract void getIntentData();

    /**
     * 标题栏标题
     */
    public TextView title;

    /**
     * 中间内容区域的容器
     */
    public LinearLayout base_content;
    /**
     * 中间内容区域的布局
     */
    private View contentView;
    /**
     * FrameLayout
     */
    public FrameLayout framelayout_root;
    /**
     * 标题栏根布局
     */
    public RelativeLayout rl_common_title;

    /**
     * 标题栏右边按钮
     */
    public TextView tv_right_text;

    /**
     * 返回按钮
     */
    public ImageView image_back;
    /**
     * 标题右侧图标
     */
    public ImageView image_right;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //设置activity主题
        setTheme(android.support.v7.appcompat.R.style.Theme_AppCompat_Light_NoActionBar);

        int sdkInt = Build.VERSION.SDK_INT;
        instance = this;
        //设置沉浸式标题栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        if (sdkInt > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {// api15 以上打开硬件加速
            if (!this.getComponentName().getClassName().equals("com.ryg.dynamicload.DLProxyActivity")) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        }
//        if (Build.VERSION.SDK_INT >= 23) {
//            int checkPermission = ContextCompat.checkSelfPermission(BaseActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
//            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
//                ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//                Log.d("TTTT", "弹出提示");
//            }
//        }

        //请求权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// ANDROID6.0 请求权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, INT_ACCESS_FINE_LOCATION);
            }
        }


        mContext = this;
        // 添加Activity到堆栈
        ActivityManager.getAppManager().addActivity(this);

        if (metric == null) {
            metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
        }
        screenWidth = metric.widthPixels;
        screenHeight = metric.heightPixels;

        super.setContentView(R.layout.activity_base_layout);
        this.init();
        String runningActivityName = MyApplication.getInstance().getRunningActivityName();
        Log.i("currentActivity:", "当前所在的Activity为:" + runningActivityName);
    }

    /**
     * 设置内容区域
     *
     * @param resId 资源文件id
     */
    @Override
    public void setContentView(int resId) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.contentView = inflater.inflate(resId, null);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        this.contentView.setLayoutParams(layoutParams);
        if (null != this.base_content) {
            this.base_content.addView(this.contentView);
        }
        getIntentData();
        findWidgets();
        initComponent();
        initListener();
        initHandler();
        asyncRetrive();
    }

    private void init() {
        this.rl_common_title = (RelativeLayout) findViewById(R.id.rl_common_title);
        this.image_back = (ImageView) findViewById(R.id.image_back);
        this.image_right = (ImageView) findViewById(R.id.common_right);
        this.title = (TextView) findViewById(R.id.tv_common_title);
        this.tv_right_text = (TextView) findViewById(R.id.tv_right_text);
        this.base_content = (LinearLayout) findViewById(R.id.base_content);
        this.framelayout_root = (FrameLayout) findViewById(R.id.framelayout_root);
    }

    /**
     * 隐藏标题栏
     */
    public void hideTitle() {
        rl_common_title.setVisibility(View.GONE);
//        setRootTopColor(colorRes);
    }

    /**
     * 设置沉浸式顶部颜色（和你当前页面顶部颜色一致）
     *
     * @param colorRes
     */
    public void setRootTopColor(int colorRes) {
        framelayout_root.setBackgroundColor(getResources().getColor(colorRes));
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        OkHttpProxy.cancel(OkHttpManager.getInstance());

        //youmi:在应用的退出逻辑里面调用此接口。
        SpotManager.getInstance(mContext).onAppExit();
        // 结束Activity&从堆栈中移除
        ActivityManager.getAppManager().finishActivity(this);
    }

    /**
     * 初始化控件
     *
     * @param id
     * @param <T>
     * @return
     */
    protected <T> T findView(int id) {
        return (T) findViewById(id);
    }

    /**
     * 初始化Listener，子类根据需要自行重写
     */
    protected void initListener() {
        return;
    }

    /**
     * 初始化Handler，子类根据需要自行重写
     */
    protected void initHandler() {
        return;
    }


    /**
     * 异步查询网络数据，子类根据需要自行重写
     */
    protected void asyncRetrive() {
        return;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
        }
        return super.dispatchKeyEvent(event);
    }

    public void showDialog(String string) {
    }

    public void closeDialog() {

    }


    public void back(View v) {
        finish();
    }

    public void finishAll(){
        // 结束所有的Activity
        ActivityManager.getAppManager().finishAllActivity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (requestCode == INT_ACCESS_FINE_LOCATION) {
                    if(grantResults.length>0){
                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            // Permission Granted
//                        Toast.makeText(this,"You Granted the permission",Toast.LENGTH_LONG).show();
                        } else {
                            // Permission Denied
//                        Toast.makeText(this,"You denied the permission",Toast.LENGTH_LONG).show();
                            Toast.makeText(this,"您禁止了地理位置权限!",Toast.LENGTH_LONG).show();
                        }
                    }

                }
                break;
            default:
        }
    }
}
