package xiaofan.insdownloader.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import xiaofan.insdownloader.R;
import xiaofan.insdownloader.adapter.DownloadAdapter;


public class MyDownloadFragment extends Fragment {

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

    private ArrayList<String> downloadPaths;

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
        mAdapter = new DownloadAdapter(downloadPaths == null ? new ArrayList<String>() : downloadPaths);
        mRecyclerView.setAdapter(mAdapter);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
    }

}
