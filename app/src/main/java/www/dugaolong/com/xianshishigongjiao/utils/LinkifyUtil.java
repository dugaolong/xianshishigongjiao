package www.dugaolong.com.xianshishigongjiao.utils;

import android.text.util.Linkify;
import android.widget.TextView;

import java.util.regex.Pattern;

/**
 * Created by zhouxiaohui on 17/4/14.
 */

public class LinkifyUtil {
    public static final String PRIVACY_POLICY_CONTENT = "隐私政策";

    public static void addCustomLink(TextView textView, String regex) {
        Pattern pattern = Pattern.compile(regex);
        String scheme = "";
        switch (regex) {
            case PRIVACY_POLICY_CONTENT:
                scheme = "xagj://webview:9999?url=300";
                break;
            default:
                break;
        }
        Linkify.addLinks(textView, pattern, scheme, null, null);
    }


}
