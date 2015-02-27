package xiaofan.insdownloader.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;

import xiaofan.insdownloader.clipboard.ClipBoardWordCopyer;

public class DaemonService extends Service {

    private PowerManager.WakeLock mWakeLock;
    private ClipBoardWordCopyer clipBoardWordCopyer;

    @Override
    public void onCreate() {
        super.onCreate();
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DAEMON_WAKELOCK");

        clipBoardWordCopyer = new ClipBoardWordCopyer(this);
        clipBoardWordCopyer.startListeningClipBoard();
        DaemonHelperService.startBeating(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            if(DaemonHelperService.HEARTBEAT_ACTION.equals(intent.getAction())){
                if(!mWakeLock.isHeld()){
                    try {
                        mWakeLock.acquire();
                        sendHeartbeat();
                    }finally {
                        mWakeLock.release();
                    }
                }
            }
        }
        return START_STICKY;
    }

    private void sendHeartbeat() {
        clipBoardWordCopyer.startListeningClipBoard();
    }

    @Override
    public void onDestroy() {
        DaemonHelperService.stopBeating(getApplicationContext());
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
