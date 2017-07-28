package www.dugaolong.com.xianshishigongjiao;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import www.dugaolong.com.xianshishigongjiao.interfaces.AdViewInstlListener;
import www.dugaolong.com.xianshishigongjiao.manager.AdViewInstlManager;

public class AdInstlActivity extends Activity implements OnClickListener,
        AdViewInstlListener {

    public static final String key1 = "SDK2017152003074228ttj4ioqh1nlyp";
    public static final String keySet[] = new String[] { key1 };
    private Button requestAd, showAd;
    public static InitConfiguration initConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_adinstl);
        requestAd = (Button) findViewById(R.id.requestad);
        requestAd.setOnClickListener(this);
        showAd = (Button) findViewById(R.id.showad);
        showAd.setOnClickListener(this);
        //获取后台配置
        initConfiguration = new InitConfiguration.Builder(this)
                .setUpdateMode(InitConfiguration.UpdateMode.EVERYTIME)
                .setInstlCloseble(InitConfiguration.InstlSwitcher.CANCLOSED)
                .setRunMode(InitConfiguration.RunMode.TEST)
                .build();
        //插屏 配置
        AdViewInstlManager.getInstance(this).init(initConfiguration, keySet);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.requestad) {
            // 只请求广告，适用于预加载
            AdViewInstlManager.getInstance(this).requestAd(this, key1, this);
        } else if (v.getId() == R.id.showad) {
            AdViewInstlManager.getInstance(this).showAd(this, key1);
        }
    }

    @Override
    public void onAdClick(String arg0) {
        Log.i("AdInstlActivity", "onAdClick");
    }

    @Override
    public void onAdDismiss(String arg0) {
        Log.i("AdInstlActivity", "onAdDismiss");
    }

    @Override
    public void onAdDisplay(String arg0) {
        Log.i("AdInstlActivity", "onDisplayAd");
    }

    @Override
    public void onAdFailed(String arg0) {
        Log.i("AdInstlActivity", "onAdFailed:" + arg0);
    }

    @Override
    public void onAdRecieved(String arg0) {
        Log.i("AdInstlActivity", "onReceivedAd");
    }

}
