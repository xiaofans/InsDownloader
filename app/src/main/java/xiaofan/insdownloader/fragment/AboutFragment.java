package xiaofan.insdownloader.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import xiaofan.insdownloader.R;
import xiaofan.insdownloader.utils.HttpCacheUtils;


public class AboutFragment extends Fragment {


    public static AboutFragment newInstance(){
        AboutFragment aboutFragment = new AboutFragment();
        return aboutFragment;
    }

    private TextView downloadPathTv;

    public AboutFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        setUpViews(view);
        return view;
    }

    private void setUpViews(View view) {
        downloadPathTv = (TextView) view.findViewById(R.id.sd_path_tv);
        if(getActivity() != null){
            downloadPathTv.setText("图片及视频存放路径:" + HttpCacheUtils.getCachedPath(getActivity()));
        }
    }
}
