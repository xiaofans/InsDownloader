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
    private DialogFragment progressDialog;
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
            progressDialog = getProgressDialog();
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
        startActivity(PhotoViewActivity.newIntent(this, successEvent.downloadedFilePath));
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
        progressDialog = getProgressDialog();
        startService(DownloadService.newIntent(UserConfirmActivity.this, photoUrl));
    }

    public DialogFragment getProgressDialog(){
        return  ProgressDialogFragment.createBuilder(this, getSupportFragmentManager())
                .setMessage("下载中")
                .setCancelable(false)
                .setRequestCode(534)
                .show();
    }


}
