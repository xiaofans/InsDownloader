package xiaofan.insdownloader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import xiaofan.insdownloader.fragment.NewDownloadPicFragment;

public class AddDownloadActivity extends BaseActivity {

  public static Intent newIntent(Context context){
      Intent intent = new Intent(context,AddDownloadActivity.class);
     return intent;
  }

  public Toolbar toolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_download);
    setUpActionbar();
    setUpNewDownloadFragment();
  }


  private void setUpActionbar() {
    // Toolbar
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle(getResources().getString(R.string.new_download));
    toolbar.setBackgroundColor(Color.argb(255, 69, 115, 153));
    toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
    setSupportActionBar(toolbar);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });

  }

  private void setUpNewDownloadFragment() {
    getSupportFragmentManager().beginTransaction().add(R.id.frame_content,
        NewDownloadPicFragment.newInstance(), "NewDownloadPicFragment").commit();
  }

}
