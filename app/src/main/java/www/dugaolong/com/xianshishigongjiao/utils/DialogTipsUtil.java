package www.dugaolong.com.xianshishigongjiao.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import www.dugaolong.com.xianshishigongjiao.R;


/**
 * 显示dialog工具类
 */
public class DialogTipsUtil {


    public static AlertDialog dialog;

    private static final String KEY_PRIVACY_POLICY = "隐私政策";


    /**
     * 专门给隐私政策提供的dialog
     * @param type
     * @param activity
     * @param title
     * @param desContent
     * @param positivebtn
     * @param onPositiveClickListener
     * @param negativebtn
     * @param onNegativeClickListener
     * @return
     */
    public static Dialog showPrivate(final int type, Activity activity, String title, final String desContent, String positivebtn,
                                     View.OnClickListener onPositiveClickListener, String negativebtn, View.OnClickListener onNegativeClickListener) {
        closeProgressDialog();

        dialog = new AlertDialog.Builder(activity, R.style.custom_dialog).create();
        dialog.setCancelable(false);
        dialog.show();
        View view = LayoutInflater.from(activity).inflate(R.layout.public_dialog, null, false);
        TextView tv_title = (TextView) view.findViewById(R.id.public_updata_title);
        TextView content = (TextView) view.findViewById(R.id.public_updata_content);
        TextView cancle = (TextView) view.findViewById(R.id.public_updata_cancle);
        TextView submit = (TextView) view.findViewById(R.id.public_updata_submit);
        View line = view.findViewById(R.id.public_updata_line3);
        dialog.setContentView(view);

        tv_title.setText(title);
        content.setText(desContent);
//        content.setGravity(Gravity.CENTER);
        submit.setText(positivebtn);

        if (type == 1) {//一个按钮,正常是两个按钮
            cancle.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        } else {
            cancle.setText(negativebtn);
        }
        if (TextUtils.isEmpty(title)) {
            tv_title.setVisibility(View.GONE);
        } else {
            tv_title.setVisibility(View.VISIBLE);
        }
        /**
         * 隐私政策
         */
        if (title.contains(KEY_PRIVACY_POLICY)) {
            LinkifyUtil.addCustomLink(content, LinkifyUtil.PRIVACY_POLICY_CONTENT);
            tv_title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
            submit.setBackgroundResource(R.drawable.public_btn_submit_private_policy);
            submit.setTextColor(activity.getResources().getColor(R.color.white));
            cancle.setTextColor(activity.getResources().getColor(R.color.app_theme_color));
            content.setTextColor(activity.getResources().getColor(R.color.black_color));
        }

        submit.setOnClickListener(onPositiveClickListener);
        cancle.setOnClickListener(onNegativeClickListener);

        return dialog;
    }

    public static void closeProgressDialog() {
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = null;
    }

}
