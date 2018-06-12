package xiaofan.insdownloader.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import xiaofan.insdownloader.R;
import xiaofan.insdownloader.UserConfirmActivity;
import xiaofan.insdownloader.clipboard.ClipBoardWordCopyer;

public class NewDownloadPicFragment extends Fragment implements View.OnClickListener{

    public static NewDownloadPicFragment newInstance(){
        NewDownloadPicFragment newDownloadPicFragment = new NewDownloadPicFragment();
        return newDownloadPicFragment;
    }

    private EditText downloadPicUrlEt;
    private Button   downloadBtn;
    public NewDownloadPicFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_download_pic, container, false);
        setUpViews(view);
        return view;
    }

    private void setUpViews(View view) {
        downloadPicUrlEt = (EditText) view.findViewById(R.id.et_new_download_pic_url);
        downloadBtn = (Button) view.findViewById(R.id.btn_download);
        downloadBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_download:
                startDownload();
                break;
        }
    }

    private void startDownload() {
        String picUrl = downloadPicUrlEt.getText().toString();
        if(TextUtils.isEmpty(picUrl)){
            Toast.makeText(getActivity(), R.string.tips_download_path_not_null,Toast.LENGTH_LONG).show();
            return;
        }
        if(!picUrl.contains(ClipBoardWordCopyer.INS_URL)){
            Toast.makeText(getActivity(), R.string.tips_not_from_ins,Toast.LENGTH_LONG).show();
            return;
        }
        startActivity(UserConfirmActivity.newIntent(getActivity(),picUrl,true));
    }
}
