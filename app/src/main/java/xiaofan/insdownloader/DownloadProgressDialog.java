package xiaofan.insdownloader;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.avast.android.dialogs.core.BaseDialogFragment;
import xiaofan.insdownloader.service.ProgressInfo;

/**
 * Created by dazhaoyu on 2016/3/22.
 */
public class DownloadProgressDialog extends BaseDialogFragment{

  private View view;
  private ProgressBar progressBar;
  private TextView downloadProgressTv;
  private TextView downloadSizeTv;
  private TextView messageTv;
  @Override
  protected Builder build(Builder builder) {
    view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_download_progress,null);
    setUpViews();
    builder.setTitle(getString(R.string.p_tips));
    builder.setView(view);
    return builder;
  }

  private void setUpViews() {
    progressBar = (ProgressBar) view.findViewById(R.id.downladProgressBar);
    downloadProgressTv = (TextView) view.findViewById(R.id.downloadPercentTv);
    downloadSizeTv = (TextView) view.findViewById(R.id.totalSizeTv);
    messageTv = (TextView) view.findViewById(R.id.messageTv);
  }

  public void setProgressInfo(ProgressInfo progressInfo){
    progressBar.setIndeterminate(false);
    if(progressInfo.progress == 0){
      downloadProgressTv.setText("");
      downloadSizeTv.setText("");
    }else{
      downloadProgressTv.setText((progressInfo.downloadedBytes / 1024) + "kb");
      downloadSizeTv.setText((progressInfo.totalBytes / 1024) +"kb");
    }
    progressBar.setProgress(progressInfo.progress);
    messageTv.setText(R.string.p_dwonloading);
  }

  public void enableParseLoading(){
    progressBar.setIndeterminate(true);
    downloadProgressTv.setText("");
    downloadSizeTv.setText("");
    messageTv.setText(R.string.parse_target_url);
  }


}
