package xiaofan.insdownloader.videoframemanager;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import java.lang.ref.WeakReference;

/**
 * Created by dazhaoyu on 2016/3/24.
 */
public class VideoFrameView extends ImageView{

  private String mVideoPath;
  private WeakReference<View> mThisView;
  private boolean mIsDrawn;
  private VideoFrameTask mDecodeThread;

  public VideoFrameView(Context context) {
    super(context);
  }

  public VideoFrameView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public String getVideoPath() {
    return mVideoPath;
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    // If the image isn't already drawn, and the URL is set
    if ((!mIsDrawn) && (mVideoPath != null)) {
      // Starts downloading this View, using the current cache setting
      mDecodeThread = VideoFrameManager.startDecode(this);
      // After successfully downloading the image, this marks that it's available.
      mIsDrawn = true;
    }
  }


  public void setVideoPath(String videoPath) {
    if(mVideoPath != null){
      if(!mVideoPath.equals(videoPath)){
        VideoFrameManager.removeDecode(mDecodeThread,videoPath);
      }else{
        return;
      }
    }

    mVideoPath = videoPath;
    if(mIsDrawn && videoPath != null){
      mDecodeThread = VideoFrameManager.startDecode(this);
    }
  }
}
