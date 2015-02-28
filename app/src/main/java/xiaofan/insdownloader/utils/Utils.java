package xiaofan.insdownloader.utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by zhaoyu on 2015/2/28.
 */
public class Utils {

    public static void fitScreenIfNeeded(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    public static void addSystemUIPadding(Activity activity, View contentView)
    {
        SystemBarTintManager.SystemBarConfig systemBarConfig = new SystemBarTintManager(activity).getConfig();
        int left = contentView.getPaddingLeft();
        int top = contentView.getPaddingTop() + systemBarConfig.getPixelInsetTop(false);
        int right = contentView.getPaddingRight() + systemBarConfig.getPixelInsetRight();
        int bottom = contentView.getPaddingBottom() + systemBarConfig.getPixelInsetBottom();
        contentView.setPadding(left,top,right,bottom);
    }
}
