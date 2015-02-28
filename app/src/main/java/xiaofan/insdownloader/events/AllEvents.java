package xiaofan.insdownloader.events;

/**
 * Created by zhaoyu on 2015/2/28.
 */
public class AllEvents {
    // 下载图片事件
    public  static class DownloadSuccessEvent{
        public String downloadedFilePath;

        public DownloadSuccessEvent(String downloadedFilePath) {
            this.downloadedFilePath = downloadedFilePath;
        }
    }

    public  static class DownloadFailureEvent{
    }
}
