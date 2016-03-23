package xiaofan.insdownloader.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;
import xiaofan.insdownloader.fragment.MyPicDownloadFragment;
import xiaofan.insdownloader.fragment.MyVideoDownloadFragment;
import xiaofan.insdownloader.utils.Utils;

/**
 * Created by dazhaoyu on 2016/3/23.
 */
public class DownloadPagerAdapter extends FragmentPagerAdapter{

  public static final int PAGER_COUNT = 2;
  private ArrayList<String> picPaths;
  private ArrayList<String> videoPaths;
  private Context context;

  public DownloadPagerAdapter(Context context,FragmentManager fm) {
    super(fm);
    this.context = context;
    videoPaths = new ArrayList<String>();
    picPaths = Utils.getMyDownloads(context,videoPaths);
  }

  @Override
  public Fragment getItem(int position) {
    switch (position){
      case 0:
        return MyPicDownloadFragment.newInstance(picPaths);
      case 1:
        return  MyVideoDownloadFragment.newInstance(videoPaths);
    }
    return null;
  }

  @Override
  public int getCount() {
    return PAGER_COUNT;
  }

  @Override
  public CharSequence getPageTitle(int position) {
    switch (position){
      case 0:
        return "图片";
      case 1:
        return "视频";
    }
    return super.getPageTitle(position);
  }
}
