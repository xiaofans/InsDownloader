package xiaofan.insdownloader.service;

/**
 * Created by dazhaoyu on 2016/3/22.
 */
public class ProgressInfo {
  public int progress;
  public long totalBytes;
  public long downloadedBytes;

  public ProgressInfo(int progress, long totalBytes, long downloadedBytes) {
    this.progress = progress;
    this.totalBytes = totalBytes;
    this.downloadedBytes = downloadedBytes;
  }
}
