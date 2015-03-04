package xiaofan.insdownloader.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import xiaofan.insdownloader.R;
import xiaofan.insdownloader.adapter.GuideAdapter;


public class UseGuideFragment extends Fragment {


    public static UseGuideFragment newInstance(){
        UseGuideFragment useGuideFragment = new UseGuideFragment();
        return useGuideFragment;
    }
    private StickyListHeadersListView stickyList;
    private GuideAdapter guideAdapter;
    private List<String> steps;
    public UseGuideFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_use_guide, container, false);
        setUpViews(view);
        return view;
    }

    private void setUpViews(View view) {
        stickyList = (StickyListHeadersListView) view.findViewById(R.id.list);
        steps = new ArrayList<String>();
        steps.add("1.选择图片下方的更多");
        steps.add("2.点击复制链接");
        steps.add("3.此时会弹出INS下载器,选择下载");
        guideAdapter = new GuideAdapter(steps,getActivity());
        stickyList.setAdapter(guideAdapter);
    }


}
