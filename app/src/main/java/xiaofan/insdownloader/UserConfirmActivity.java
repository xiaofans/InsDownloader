package xiaofan.insdownloader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;


import com.avast.android.dialogs.fragment.ProgressDialogFragment;
import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.ISimpleDialogListener;
import com.squareup.otto.Subscribe;

import xiaofan.insdownloader.events.AllEvents;
import xiaofan.insdownloader.service.DownloadService;
import xiaofan.insdownloader.service.ProgressInfo;

public class UserConfirmActivity extends BaseActivity implements ISimpleDialogListener {

    public static Intent newIntent(Context context,String insPhotoUrl){
        Intent intent = new Intent(context,UserConfirmActivity.class);
        intent.putExtra(INTENT_URL_PARAMS,insPhotoUrl);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public static Intent newIntent(Context context, String picUrl, boolean isInputUrl) {
        Intent intent = new Intent(context,UserConfirmActivity.class);
        intent.putExtra(INTENT_URL_PARAMS,picUrl);
        intent.putExtra(INTENT_IS_INPUT_URL,isInputUrl);
        return intent;
    }

    public static final String INTENT_URL_PARAMS = "ins_photo_url";
    public static final String INTENT_IS_INPUT_URL = "is_input_url";
    private String photoUrl;
    private DownloadProgressDialog progressDialog;
    private boolean isInputUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_user_confirm);
        photoUrl = getIntent().getStringExtra(INTENT_URL_PARAMS);
        isInputUrl = getIntent().getBooleanExtra(INTENT_IS_INPUT_URL,false);
        showConfirmDialog();
    }

    private void showConfirmDialog() {
        if(isInputUrl){
            progressDialog = new DownloadProgressDialog();
            progressDialog.setCancelable(false);
            progressDialog.show(getSupportFragmentManager(),"pbc");
            startService(DownloadService.newIntent(UserConfirmActivity.this, photoUrl));
        }else{
            SimpleDialogFragment.createBuilder(this, getSupportFragmentManager()).setTitle("INS下载器").setMessage("下载该图片?").setPositiveButtonText("确定").setNegativeButtonText("取消").show();
        }
    }

    @Subscribe
    public void downloadSuccess(AllEvents.DownloadSuccessEvent successEvent){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
        if(successEvent.isVideo){
            Toast.makeText(this,"下载已完成，请打开下载器查看",Toast.LENGTH_LONG).show();
        }else{
            startActivity(PhotoViewActivity.newIntent(this, successEvent.downloadedFilePath));
        }
        finish();
    }

    @Subscribe
    public void downloadFailure(AllEvents.DownloadFailureEvent failureEvent){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
        Toast.makeText(this,"下载失败",Toast.LENGTH_LONG).show();
        finish();
    }

    @Subscribe
    public void downloadProgress(AllEvents.DownloadStatusEvent statusEvent){
        if(statusEvent.status == AllEvents.DownloadStatusEvent.STATE_PARSE_URL){
          progressDialog.enableParseLoading();
        }else{
          if(statusEvent.progressInfo == null) statusEvent.progressInfo = new ProgressInfo(0,0,0);
            progressDialog.setProgressInfo(statusEvent.progressInfo);
        }
    }

    @Override
    public void onNegativeButtonClicked(int i) {
        finish();
    }

    @Override
    public void onNeutralButtonClicked(int i) {
        finish();
    }

    @Override
    public void onPositiveButtonClicked(int i) {
        progressDialog = new DownloadProgressDialog();
        progressDialog.setCancelable(false);
        progressDialog.show(getSupportFragmentManager(),"pbc");
        startService(DownloadService.newIntent(UserConfirmActivity.this, photoUrl));
    }




}
