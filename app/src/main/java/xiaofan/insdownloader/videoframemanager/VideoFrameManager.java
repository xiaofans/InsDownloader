package xiaofan.insdownloader.videoframemanager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by dazhaoyu on 2016/3/24.
 */
public class VideoFrameManager {

  static final int DECODE_STARTED = 1;
  static final int DECODE_FINISHED = 2;


  private static final int CORE_POOL_SIZE = 8;
  private static final int MAXIMUM_POOL_SIZE = 8;
  private static final int KEEP_ALIVE_TIME = 1;
  private static final TimeUnit KEEP_ALIVE_TIME_UNIT;

  private final BlockingQueue<Runnable> mDecodeWorkQueue;
  private final ThreadPoolExecutor mDecodeThreadPool;
  private final Queue<VideoFrameTask> mVideoFrameTaskQuene;
  private Handler mHandler;

  private static VideoFrameManager sInstance = null;

  static {
    KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    sInstance = new VideoFrameManager();
  }

  private VideoFrameManager(){
    mDecodeWorkQueue = new LinkedBlockingQueue<Runnable>();
    mVideoFrameTaskQuene = new LinkedBlockingDeque<VideoFrameTask>();
    mDecodeThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
        KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, mDecodeWorkQueue);

    mHandler = new Handler(Looper.getMainLooper()){
      @Override
      public void handleMessage(Message msg) {
        super.handleMessage(msg);
          VideoFrameTask videoFrameTask = (VideoFrameTask) msg.obj;
         VideoFrameView videoFrameView = videoFrameTask.getVideoFrameView();
         String path = videoFrameView.getVideoPath();
         if(path.equals(videoFrameTask.getVideoPath())){
           videoFrameView.setImageBitmap(videoFrameTask.getmBitmap());
           recycleTask(videoFrameTask);
         }

      }
    };
  }

  private void recycleTask(VideoFrameTask videoFrameTask) {
    // Frees up memory in the task
    videoFrameTask.recycle();
    // Puts the task object back into the queue for re-use.
    mVideoFrameTaskQuene.offer(videoFrameTask);
  }

  public static VideoFrameManager getInstance() {
    return sInstance;
  }

  public static void cancelAll(){

    VideoFrameTask[] taskArray = new VideoFrameTask[sInstance.mDecodeWorkQueue.size()];
    sInstance.mDecodeWorkQueue.toArray(taskArray);
    int taskArraylen = taskArray.length;
    synchronized (sInstance) {
      for (int taskArrayIndex = 0; taskArrayIndex < taskArraylen; taskArrayIndex++) {
        Thread thread = taskArray[taskArrayIndex].mThreadThis;
        if (null != thread) {
          thread.interrupt();
        }
      }
    }

  }


 public static void removeDecode(VideoFrameTask videoFrameTask,String videoPath){
    if(videoFrameTask != null && videoFrameTask.getVideoPath().equals(videoPath)){
      synchronized (sInstance){
        // Gets the Thread that the downloader task is running on
        Thread thread = videoFrameTask.getCurrentThread();
        // If the Thread exists, posts an interrupt to it
        if (null != thread)
          thread.interrupt();
      }
    }

 }

  static public  VideoFrameTask startDecode(VideoFrameView videoFrameView){
   VideoFrameTask videoFrameTask = sInstance.mVideoFrameTaskQuene.poll();
    if(videoFrameTask == null){
      videoFrameTask = new VideoFrameTask();
    }
    videoFrameTask.initializeDecoderTask(VideoFrameManager.sInstance, videoFrameView);
    sInstance.mDecodeThreadPool.execute(videoFrameTask.getmDecodeRunnable());
    return videoFrameTask;
 }

  public void handleState(VideoFrameTask videoFrameTask,int decodeState) {
    Message completeMessage = mHandler.obtainMessage(decodeState, videoFrameTask);
    completeMessage.sendToTarget();
  }
}
