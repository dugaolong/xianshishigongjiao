package www.dugaolong.com.xianshishigongjiao;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

/**
 * 系统组件
 */
public class MyApplication extends Application {
    private static Context appContext;
    public static MyApplication instance;
    private Bitmap screenShot;
    String TAG = "MyApplication";
    private String versionName;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        instance = this;

//        AdSdk.setDebugOn(); // 打开调试，输出调试信息
//        AdSdk.setMockOn();  // 调试时打开，正式发布时关闭
        //配置小米广告的sdk
//        AdSdk.initialize(this, "2882303761517555679");
    }

    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }

    public static Context getAppContext() {
        return appContext;
    }

    //截屏
    public void setScreenShot(Bitmap screenShot) {
        this.screenShot = screenShot;
    }

    public Bitmap getScreenShot() {
        return screenShot;
    }


    public String getRunningActivityName() {
        android.app.ActivityManager mActivityManager = (android.app.ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        return mActivityManager.getRunningTasks(1).get(0).topActivity.getClassName();
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        if (info == null) {
            info = new PackageInfo();
        }
        return info;
    }
    /**
     * 获取版本号
     *
     * @return
     */
    public String getVersionName() {
        if (versionName == null) {
            PackageInfo packageInfo = this.getPackageInfo();
            versionName = packageInfo.versionName;
        }
        return versionName;
    }


}
