package xiaofan.insdownloader.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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



    public static ArrayList<String> getMyDownloads(Context context,ArrayList<String> videos) {
        if(videos == null){
            videos = new ArrayList<String>();
        }
        String downloadedPaths = HttpCacheUtils.getCachedPath(context) + File.separator;
        ArrayList<String> list = new ArrayList<String>();
        File f = new File(downloadedPaths);
        if(f.exists() && f.listFiles() != null && f.listFiles().length > 0){
            File[] files = f.listFiles();
            for(int i = files.length - 1; i >= 0;i--){
                if(files[i].getAbsolutePath().endsWith(".mp4")){
                    videos.add(files[i].getAbsolutePath());
                }else{
                    list.add(files[i].getAbsolutePath());
                }
            }
        }
        return list;
    }
}
