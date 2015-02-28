package xiaofan.insdownloader.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


/**
 * Created by zhaoyu on 2014/10/9.
 * 1.background panning
 * 2.add fackbook spring
 * 3.learn how to draw
 */
//--------------------------------------------------
/**
  private void performTraversals(){
    final View host = mView;
    host.measure... onMeasure()
    host.layout...  onLayout()
    host.draw....   onDraw
 }
  onMeasure 决定视图的大小
  onLayout 决定视图的位置
  onDraw   将视图呈现在画布上（
  Draw traversal performs several drawing steps which must be executed
  in the appropriate order:

  1. Draw the background
  2. If necessary, save the canvas' layers to prepare for fading
  3. Draw view's content
  4. Draw children
  5. If necessary, draw the fading edges and restore layers
  6. Draw decorations (scrollbars for instance)
 ）
 measure过程确定视图的大小 而layout过程确定视图的位置
 loyout是从view的layout方法开始的
 from
 1.http://blog.csdn.net/xyz_lmn/article/details/20385049
 2.http://developer.android.com/guide/topics/ui/how-android-draws.html
 3.http://www.2cto.com/kf/201312/267855.html
 （2 3 need to look）
 */
//--------------------------------------------------

/**
 ayalysis the code.
 setWillNotDraw(false);
 !implements Target.
 不用ImageView 因为还要加其他元素
 so good.
 I'm so happy it's alerday done! on date 2014.10.18
 */

public class PanningBackgroundFrameLayout extends FrameLayout implements View.OnClickListener,Target{

   private static final String TAG = PanningBackgroundFrameLayout.class.getSimpleName();
   private boolean isPanningEnabled;
   private boolean canPan = true;
   private boolean isAnimatingBackground;
   private BitmapDrawable background;
   private int backgroundColor = -13421773;
   private int backgroundHeight;
   private int backgroundWidth;
   private double backgroundOffset;
   private double backgroundScale;
   private double minBackgroundOffset;
   private double minBackgroundScale;
   private double panPerSecond = 10F * getResources().getDisplayMetrics().density;
   private long lastPan;
   private boolean isZoomedOut = false;
   private boolean shouldAnimateBackgroundChange = true;
    private boolean isClickToZoomEnabled;

    private Runnable updateOffset = new Runnable() {
        @Override
        public void run() {
            if(isPanningEnabled && canPan){
                if(backgroundOffset < 0.0D){
                    if(backgroundOffset < minBackgroundOffset){
                        panPerSecond = -panPerSecond;
                        backgroundOffset = minBackgroundOffset;
                    }
                }else{
                    panPerSecond = -panPerSecond;
                    backgroundOffset = 0.0D;
                }
                double d = panPerSecond *(System.currentTimeMillis() - lastPan) / 1000.0D;
                backgroundOffset += d;
                lastPan = System.currentTimeMillis();
                ViewCompat.postInvalidateOnAnimation(PanningBackgroundFrameLayout.this);
                postDelayed(this,16L);
            }

        }
    };

    private SpringSystem springSystem = SpringSystem.create();
    private Spring scaleSpring = springSystem.createSpring();
    private SpringListener springListener = new SpringListener() {
        @Override
        public void onSpringUpdate(Spring spring) {
            backgroundScale = spring.getCurrentValue();
            ViewCompat.postInvalidateOnAnimation(PanningBackgroundFrameLayout.this);
        }

        @Override
        public void onSpringAtRest(Spring spring) {
            if(isZoomedOut){
                setPanningEnabled(false);
            }else {
                setPanningEnabled(true);
            }
        }

        @Override
        public void onSpringActivate(Spring spring) {

        }

        @Override
        public void onSpringEndStateChange(Spring spring) {

        }
    };

    public PanningBackgroundFrameLayout(Context context) {
        this(context,null);
    }

    public PanningBackgroundFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        scaleSpring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(60.0D, 9.0D));
        scaleSpring.setCurrentValue(1.0D);
        scaleSpring.addListener(springListener);
        setWillNotDraw(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureBackground();
        setPanningEnabled(isPanningEnabled);
    }

    private void measureBackground() {
        if(background == null) return;
        // step 1. calculate background width and height
        this.backgroundHeight = background.getBitmap().getHeight();
        this.backgroundWidth =  background.getBitmap().getWidth();
        float f = (float)getMeasuredHeight() / (float)backgroundHeight;
        if(f * backgroundWidth < getMeasuredWidth()){
            f = (float)getMeasuredWidth() / (float)backgroundWidth;
        }
        backgroundWidth = (int)(f * backgroundWidth);
        backgroundHeight = (int)(f * backgroundHeight);
        // step 2. calculate background scale offset can pan.
        if(backgroundWidth >= getWidth()){
            minBackgroundScale = (double)getMeasuredWidth() / (double)backgroundWidth;
            minBackgroundOffset = getMeasuredWidth() - backgroundWidth;
            if(minBackgroundScale >= 0.9d){
                canPan = false;
                minBackgroundScale = (double)getMeasuredHeight() / (double)backgroundHeight;
                minBackgroundOffset = getMeasuredHeight() - backgroundHeight;
                backgroundScale = 1.0D;
                backgroundOffset = (double)(getMeasuredHeight() - backgroundHeight) / 2;
            } else {
                canPan = true;
                backgroundScale = 1.0D;
                backgroundOffset = (double)(getMeasuredWidth() - backgroundWidth) / 2;
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(background != null){
            if(backgroundScale != 1.0D || isAnimatingBackground){
                canvas.drawColor(backgroundColor);
            }
            float f = 1.0f;
            int left,top,right,bottom;
            f = (float)((backgroundScale - minBackgroundScale) / (1.0D - minBackgroundScale));
            if(backgroundWidth > getWidth()){
                left = (int)(f * backgroundOffset);
                top = (int)(getHeight() - getHeight() * backgroundScale) / 2;
                right = left + (int)(backgroundWidth * backgroundScale);
                bottom = (int)(top + getHeight() * backgroundScale);
            }else{
                left = (int)(getWidth() - getWidth() * backgroundScale)/2;
                top =  (int)(f * backgroundOffset);
                right = (int)(left + getWidth() * backgroundScale);
                bottom = (int)(top + backgroundHeight * backgroundScale);
            }
            background.setBounds(left,top,right,bottom);
            background.draw(canvas);
            if (Math.max(f, 0.0F) != 0.0F) {
                canvas.drawColor(Color.argb((int)(85.0F * Math.max(f, 0.0F)), 0, 0, 0));
            }
        }else{
            canvas.drawColor(backgroundColor);
        }
    }


    public void setPanningEnabled(boolean isPanningEnabled) {
        this.isPanningEnabled = isPanningEnabled;
        removeCallbacks(updateOffset);
        if(isPanningEnabled && canPan){
            lastPan = System.currentTimeMillis();
            postDelayed(updateOffset,16L);
        }
    }

    public void setPanningBackground(Bitmap bitmap) {
       if(bitmap == null){
           this.background = null;
           ViewCompat.postInvalidateOnAnimation(this);
           return;
       }
        this.background = new BitmapDrawable(getResources(),bitmap);
        measureBackground();
        ViewCompat.postInvalidateOnAnimation(this);
        if(shouldAnimateBackgroundChange){
            ObjectAnimator objectAnimator = ObjectAnimator.ofInt(background,"alpha",new int[]{0,255});
            objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ViewCompat.postInvalidateOnAnimation(PanningBackgroundFrameLayout.this);
                }
            });
            objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    isAnimatingBackground = false;
                    ViewCompat.postInvalidateOnAnimation(PanningBackgroundFrameLayout.this);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            isAnimatingBackground = true;
            objectAnimator.start();
        }
        
    }



    public void toggleZoomedOut(){
        if(!canPan) return;
        if(isZoomedOut){
            scaleSpring.setVelocity(10.0D);
            scaleSpring.setEndValue(1.0D);
            isZoomedOut = false;
        }else{
            scaleSpring.setVelocity(-10.0D);
            scaleSpring.setEndValue(this.minBackgroundScale);
            isZoomedOut = true;
        }

    }

    public void setClickToZoomEnabled(boolean enabled){
         isClickToZoomEnabled= enabled;
        if(isClickToZoomEnabled){
            setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        toggleZoomedOut();
    }

    public void setShouldAnimateBackgroundChange(boolean shouldAnimateBackgroundChange)
    {
        this.shouldAnimateBackgroundChange = shouldAnimateBackgroundChange;
    }

    public boolean shouldAnimateBackgroundChange()
    {
        return this.shouldAnimateBackgroundChange;
    }

    public void setBackgroundColor(int color){
        this.backgroundColor = color;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
        setPanningBackground(bitmap);
    }

    @Override
    public void onBitmapFailed(Drawable drawable) {
        this.background = null;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override
    public void onPrepareLoad(Drawable drawable) {

    }



    public boolean isZoomedOut() {
        return isZoomedOut;
    }
}
