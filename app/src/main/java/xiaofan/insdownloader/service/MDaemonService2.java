package xiaofan.insdownloader.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by dazhaoyu on 2016/3/21.
 */
public class MDaemonService2 extends Service{

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return Service.START_NOT_STICKY;
  }
}
