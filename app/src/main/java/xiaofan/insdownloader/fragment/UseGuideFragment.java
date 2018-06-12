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
        steps.add(getString(R.string.ug_step1));
        steps.add(getString(R.string.ug_step2));
        steps.add(getString(R.string.ug_step3));
        guideAdapter = new GuideAdapter(steps,getActivity());
        stickyList.setAdapter(guideAdapter);
    }


}
