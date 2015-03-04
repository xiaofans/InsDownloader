package xiaofan.insdownloader.fragment;


import android.app.Activity;
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


public class MyDownloadFragment extends Fragment{

    public static MyDownloadFragment newInstance(ArrayList<String> downloadPaths){
        MyDownloadFragment myDownloadFragment = new MyDownloadFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("paths",downloadPaths);
        myDownloadFragment.setArguments(args);
        return myDownloadFragment;
    }

    private RecyclerView mRecyclerView;
    private FloatingActionButton floatingActionButton;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Animation animation;



    private ArrayList<String> downloadPaths;

    private Handler handler = new Handler();

    public MyDownloadFragment() {
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(getArguments() != null){
            downloadPaths = getArguments().getStringArrayList("paths");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_download, container, false);
        setUpViews(view);
        return view;
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
        mAdapter = new DownloadAdapter(getActivity(),downloadPaths == null ? new ArrayList<String>() : downloadPaths);
        mRecyclerView.setAdapter(mAdapter);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                setUpDownloadFragment();
            }
        });
        animation = AnimationUtils.loadAnimation(getActivity(),R.anim.fab_newtab);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },1000);
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
