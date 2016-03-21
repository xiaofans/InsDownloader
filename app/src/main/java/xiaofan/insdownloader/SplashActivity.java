package xiaofan.insdownloader;

import android.os.Bundle;
import com.facebook.shimmer.ShimmerFrameLayout;

public class SplashActivity extends BaseActivity {

  private ShimmerFrameLayout shimmerFrameLayout;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    setUpViews();
  }

  private void setUpViews() {
    shimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
    shimmerFrameLayout.startShimmerAnimation();
  }
}
