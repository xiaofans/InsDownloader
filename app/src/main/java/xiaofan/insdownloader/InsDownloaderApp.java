package xiaofan.insdownloader;

import com.marswin89.marsdaemon.DaemonApplication;
import com.marswin89.marsdaemon.DaemonConfigurations;
import xiaofan.insdownloader.receiver.MDaemonReceiver;
import xiaofan.insdownloader.receiver.MDaemonReceiver2;
import xiaofan.insdownloader.service.MDaemonService;
import xiaofan.insdownloader.service.MDaemonService2;

/**
 * Created by dazhaoyu on 2016/3/21.
 */
public class InsDownloaderApp extends DaemonApplication{

  @Override
  public void onCreate() {
    super.onCreate();
  }

  @Override protected DaemonConfigurations getDaemonConfigurations() {
    DaemonConfigurations.DaemonConfiguration configuration = new DaemonConfigurations.DaemonConfiguration("xiaofan.insdownloader:process1",
        MDaemonService.class.getCanonicalName(), MDaemonReceiver.class.getCanonicalName());
    DaemonConfigurations.DaemonConfiguration configuration2 = new DaemonConfigurations.DaemonConfiguration("xiaofan.insdownloader:process2",
        MDaemonService2.class.getCanonicalName(), MDaemonReceiver2.class.getCanonicalName());
    return new DaemonConfigurations(configuration,configuration2);
  }
}
