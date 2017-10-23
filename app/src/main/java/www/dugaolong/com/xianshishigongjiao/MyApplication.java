package www.dugaolong.com.xianshishigongjiao;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.util.Log;

import com.xiaomi.ad.AdSdk;

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
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        instance = this;

//        AdSdk.setDebugOn(); // 打开调试，输出调试信息
//        AdSdk.setMockOn();  // 调试时打开，正式发布时关闭
        //配置小米广告的sdk
        AdSdk.initialize(this, "2882303761517555679");
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


    //检查apk的哈希值
    public int checkAPP(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                            PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];

            int hashcode = sign.hashCode();
            Log.i("test", "hashCode : " + hashcode);
            return hashcode == -82892576 ? 1 : 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    //debug默认签名中含有的信息
    private final static X500Principal DEBUG_DN = new X500Principal("CN=Android Debug,O=Android,C=US");

    //判断是否是debug版本，用来数据库加密和log自动判断,true表示debug版本，false表示release版本
    public boolean isDebuggable(Context ctx) {
        boolean debuggable = false;
        try {
            PackageInfo pinfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature signatures[] = pinfo.signatures;
            for (int i = 0; i < signatures.length; i++) {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                ByteArrayInputStream stream = new ByteArrayInputStream(signatures[i].toByteArray());
                X509Certificate cert = (X509Certificate) cf.generateCertificate(stream);
                // 判断是否含有debug默认的签名信息
                debuggable = cert.getSubjectX500Principal().equals(DEBUG_DN);
                if (debuggable) {
                    break;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return debuggable;
    }


}
