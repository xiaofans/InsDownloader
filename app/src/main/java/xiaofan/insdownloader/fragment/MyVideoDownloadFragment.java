package xiaofan.insdownloader.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.melnykov.fab.FloatingActionButton;
import java.util.ArrayList;
import xiaofan.insdownloader.R;
import xiaofan.insdownloader.adapter.DownloadAdapter;
import xiaofan.insdownloader.adapter.DownloadVideoAdapter;
import xiaofan.insdownloader.utils.Utils;

/**
 * Created by dazhaoyu on 2016/3/23.
 */
public class MyVideoDownloadFragment extends Fragment{


  public static MyVideoDownloadFragment newInstance(ArrayList<String> videoPaths){
    MyVideoDownloadFragment myVideoDownloadFragment = new MyVideoDownloadFragment();
    Bundle args = new Bundle();
    args.putStringArrayList("paths",videoPaths);
    myVideoDownloadFragment.setArguments(args);
    return myVideoDownloadFragment;
  }

  private RecyclerView mRecyclerView;
  private FloatingActionButton floatingActionButton;
  private RecyclerView.LayoutManager mLayoutManager;
  private DownloadVideoAdapter mAdapter;
  private SwipeRefreshLayout swipeRefreshLayout;
  private Animation animation;
  private ArrayList<String> videoPaths;
  private Handler handler = new Handler();

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    if(getArguments() != null){
      videoPaths = getArguments().getStringArrayList("paths");
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View convertView = inflater.inflate(R.layout.fragment_my_video_download,container,false);
    return convertView;
  }


  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setUpViews(view);
  }


  private void setUpViews(View view) {
    mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
    mRecyclerView.setHasFixedSize(true);
    mLayoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(mLayoutManager);
    mAdapter = new DownloadVideoAdapter(getActivity(),videoPaths == null ? new ArrayList<String>() : videoPaths);
    mRecyclerView.setAdapter(mAdapter);
    floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
    floatingActionButton.setOnClickListener(new View.OnClickListener(){

      @Override
      public void onClick(View v) {
        setUpDownloadFragment();
      }
    });
    animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_newtab);
    swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        handler.postDelayed(new Runnable() {
          @Override public void run() {
            videoPaths = new ArrayList<String>();
            Utils.getMyDownloads(getActivity(), videoPaths);
            mAdapter.setDownloadPaths(videoPaths);
            swipeRefreshLayout.setRefreshing(false);
          }
        }, 1000);
      }
    });
  }


  public void setUpDownloadFragment(){
    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, NewDownloadPicFragment.newInstance(),"NewDownloadPicFragment").commit();
  }

  @Override
  public void onResume() {
    super.onResume();
    floatingActionButton.attachToRecyclerView(mRecyclerView);
    floatingActionButton.setAnimation(animation);
  }


}
