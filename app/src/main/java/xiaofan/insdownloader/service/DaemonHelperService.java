package xiaofan.insdownloader.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

public class DaemonHelperService extends Service {

    public static void startBeating(Context context){
        context.startService(new Intent(context,DaemonHelperService.class));
    }

    public static void stopBeating(Context context){
        context.stopService(new Intent(context,DaemonHelperService.class));
    }

    public static final String HEARTBEAT_ACTION = "xiaofan.insdownloader.SERVICE.HEARTBEAT";
    private PendingIntent mPendingIntent;
    private Intent mRelayIntent;
    private static final long HEARTBEAT_INTERVAL = 60 * 1000;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mPendingIntent = PendingIntent.getService(this,0,new Intent(HEARTBEAT_ACTION,null,this,DaemonHelperService.class),0);
        this.mRelayIntent = new Intent(HEARTBEAT_ACTION,null,this,DaemonService.class);
        startHeartbeat(HEARTBEAT_INTERVAL);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null && HEARTBEAT_ACTION.equals(intent.getAction())){
            startHeartbeat(HEARTBEAT_INTERVAL);
            startService(mRelayIntent);
        }
        return START_STICKY;
    }

    private void startHeartbeat(long interval) {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(mPendingIntent);
        if(interval > 0){
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + interval,interval,mPendingIntent);
        }
    }

    @Override
    public void onDestroy() {
        startHeartbeat(0);
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
