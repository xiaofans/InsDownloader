package xiaofan.insdownloader.videoframemanager;

import android.graphics.Bitmap;
import java.lang.ref.WeakReference;

/**
 * Created by dazhaoyu on 2016/3/24.
 */
public class VideoFrameTask implements VideoFrameDecodeRunnable.TaskRunnableDecodeMethods {

  private static VideoFrameManager sVideoFrameManager;
  private String mVideoPath;
  private Runnable mDecodeRunnable;
  private WeakReference<VideoFrameView> mVideoWeakRef;
  private Thread mCurrentThread;
  private Bitmap mBitmap;

  Thread mThreadThis;

  VideoFrameTask(){
    mDecodeRunnable = new VideoFrameDecodeRunnable(this);
    sVideoFrameManager = VideoFrameManager.getInstance();
  }


  void initializeDecoderTask(VideoFrameManager videoFrameManager,VideoFrameView videoFrameView){
    sVideoFrameManager = videoFrameManager;
    mVideoWeakRef = new WeakReference<VideoFrameView>(videoFrameView);
    mVideoPath = videoFrameView.getVideoPath();
  }

  @Override
  public void setDecodeThread(Thread currentThread) {
    setCurrentThread(currentThread);
  }

  public void setCurrentThread(Thread thread) {
    synchronized(sVideoFrameManager) {
      mCurrentThread = thread;
    }
  }

  public Thread getCurrentThread() {
    synchronized(sVideoFrameManager) {
      return mCurrentThread;
    }
  }


  @Override
  public String getVideoPath() {
    return mVideoPath;
  }

  @Override
  public void handleDecodeState(int state) {
    if(state == VideoFrameDecodeRunnable.STATE_DECODE_START){
      sVideoFrameManager.handleState(this,VideoFrameManager.DECODE_STARTED);
    }else if(state == VideoFrameDecodeRunnable.STATE_DECODE_COMPLETE){
      sVideoFrameManager.handleState(this,VideoFrameManager.DECODE_FINISHED);
    }
  }

  @Override
  public void setFrameBitmap(Bitmap bitmap) {
      this.mBitmap = bitmap;
  }


  public Bitmap getmBitmap() {
    return mBitmap;
  }

  public Runnable getmDecodeRunnable() {
    return mDecodeRunnable;
  }

  public VideoFrameView getVideoFrameView(){
    if(null != mVideoWeakRef){
      return mVideoWeakRef.get();
    }else{
      return null;
    }
  }

  public void recycle() {
    if ( null != mVideoWeakRef ) {
      mVideoWeakRef.clear();
      mVideoWeakRef = null;
    }
    mBitmap = null;
  }
}

