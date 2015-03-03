package xiaofan.insdownloader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


import com.squareup.otto.Subscribe;

import xiaofan.insdownloader.events.AllEvents;
import xiaofan.insdownloader.service.DownloadService;


public class UserConfirmActivity extends BaseActivity {

    public static Intent newIntent(Context context,String insPhotoUrl){
        Intent intent = new Intent(context,UserConfirmActivity.class);
        intent.putExtra(INTENT_URL_PARAMS,insPhotoUrl);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public static final String INTENT_URL_PARAMS = "ins_photo_url";
    private String photoUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_user_confirm);
        photoUrl = getIntent().getStringExtra(INTENT_URL_PARAMS);
        showConfirmDialog();
    }

    private void showConfirmDialog() {
        new AlertDialog.Builder(this).setTitle("提示").setMessage("下载该图片?").setNegativeButton("确定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startService(DownloadService.newIntent(UserConfirmActivity.this,photoUrl));
                dialog.dismiss();
            }
        }).setPositiveButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    @Subscribe
    public void downloadSuccess(AllEvents.DownloadSuccessEvent successEvent){
        startActivity(PhotoViewActivity.newIntent(this,successEvent.downloadedFilePath));
        finish();
    }

    @Subscribe
    public void downloadFailure(AllEvents.DownloadFailureEvent failureEvent){
        Toast.makeText(this,"下载失败",Toast.LENGTH_LONG).show();
        finish();
    }

}
