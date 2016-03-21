package xiaofan.insdownloader.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import xiaofan.insdownloader.clipboard.ClipBoardWordCopyer;

/**
 * Created by dazhaoyu on 2016/3/21.
 */
public class MDaemonService extends Service{

  private ClipBoardWordCopyer clipBoardWordCopyer;

  @Override
  public void onCreate() {
    super.onCreate();

  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    clipBoardWordCopyer = new ClipBoardWordCopyer(this);
    clipBoardWordCopyer.startListeningClipBoard();

    return super.onStartCommand(intent, flags, startId);
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}
