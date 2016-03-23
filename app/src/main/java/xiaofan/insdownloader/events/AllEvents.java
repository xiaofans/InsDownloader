package xiaofan.insdownloader.events;

import xiaofan.insdownloader.service.ProgressInfo;

/**
 * Created by zhaoyu on 2015/2/28.
 */
public class AllEvents {
    // 下载图片事件
    public  static class DownloadSuccessEvent{
        public String downloadedFilePath;
       public  boolean isVideo;

      public DownloadSuccessEvent(String downloadedFilePath, boolean isVideo) {
        this.downloadedFilePath = downloadedFilePath;
        this.isVideo = isVideo;
      }
    }

    public  static class DownloadFailureEvent{
    }

    public static class DownloadStatusEvent{
        public static final int STATE_PARSE_URL = 1;
        public static final int STATE_DOWNLOADING = 2;
        public int status;
        public ProgressInfo progressInfo;

        public DownloadStatusEvent(int status) {
            this.status = status;
        }

        public DownloadStatusEvent(int status, ProgressInfo progressInfo) {
            this.status = status;
            this.progressInfo = progressInfo;
        }
    }
}
