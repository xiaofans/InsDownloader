package xiaofan.insdownloader.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import xiaofan.insdownloader.utils.Logger;


/**
 * Created by zhaoyu on 2015/3/4.
 */
public class BootCompleteReceiver extends BroadcastReceiver{

    private static final String TAG = "BootCompleteReceiver";
    public static final String SERVICE_ACTION = "xiaofan.insdownloader.DEAMON_SERVICE";

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.w(TAG,"boot onReceive...");
        Intent serviceIntent = new Intent();
        serviceIntent.setAction(SERVICE_ACTION);
        context.startService(serviceIntent);
    }
}
