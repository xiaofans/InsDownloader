package xiaofan.insdownloader.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import xiaofan.insdownloader.R;
import xiaofan.insdownloader.adapter.DownloadPagerAdapter;

/**
 * Created by dazhaoyu on 2016/3/23.
 */
public class MyDownloadFragment extends Fragment{

  public static MyDownloadFragment newInstance(){
    MyDownloadFragment myDownloadFragment = new MyDownloadFragment();
    return myDownloadFragment;
  }


  private ViewPager viewPager;
  private DownloadPagerAdapter pagerAdapter;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View convertView = inflater.inflate(R.layout.fragment_my_download,container,false);
    setUpViews(convertView);
    return convertView;
  }


  private void setUpViews(View convertView) {
    viewPager = (ViewPager) convertView.findViewById(R.id.pager);
    pagerAdapter = new DownloadPagerAdapter(getActivity(),getChildFragmentManager());
    viewPager.setAdapter(pagerAdapter);
  }




}
