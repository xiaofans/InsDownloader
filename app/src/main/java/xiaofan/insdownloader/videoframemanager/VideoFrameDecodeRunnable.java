package xiaofan.insdownloader.videoframemanager;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

/**
 * Created by dazhaoyu on 2016/3/24.
 */
public class VideoFrameDecodeRunnable implements Runnable{

  static final int STATE_DECODE_COMPLETE = 0;

  final TaskRunnableDecodeMethods mVideoFrameTask;
  interface TaskRunnableDecodeMethods {
    void setDecodeThread(Thread currentThread);
    String getVideoPath();
    void handleDecodeState(int state);
    void setFrameBitmap(Bitmap bitmap);
  }

  public VideoFrameDecodeRunnable(TaskRunnableDecodeMethods mVideoFrameTask) {
    this.mVideoFrameTask = mVideoFrameTask;
  }

  @Override
  public void run() {
    mVideoFrameTask.setDecodeThread(Thread.currentThread());
    // Moves the current Thread into the background
    android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
    Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(mVideoFrameTask.getVideoPath(), MediaStore.Video.Thumbnails.MINI_KIND);
    mVideoFrameTask.setFrameBitmap(bitmap);
    mVideoFrameTask.handleDecodeState(STATE_DECODE_COMPLETE);
  }


}
