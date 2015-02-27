package xiaofan.insdownloader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import java.net.MalformedURLException;
import java.net.URL;

import xiaofan.insdownloader.insparser.Parser;


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
        setContentView(R.layout.activity_user_confirm);
        photoUrl = getIntent().getStringExtra(INTENT_URL_PARAMS);
        showConfirmDialog();
    }

    private void showConfirmDialog() {
        new AlertDialog.Builder(this).setTitle("提示").setMessage("下载该图片?").setNegativeButton("确定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FetchInsTask fetchInsTask = new FetchInsTask();
                fetchInsTask.execute();
                dialog.dismiss();
            }
        }).setPositiveButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    class FetchInsTask extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... params) {
            String url =  Parser.parseUrl(photoUrl);
            String fileName = url.substring(url.lastIndexOf("/"));
            try {
                URL url1 = new URL(url);
                
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return url;
        }

        @Override
        protected void onPostExecute(String photoUrl) {
            super.onPostExecute(photoUrl);

        }
    }

}
