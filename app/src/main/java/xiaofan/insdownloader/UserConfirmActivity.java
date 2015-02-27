package xiaofan.insdownloader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import xiaofan.insdownloader.insparser.Parser;
import xiaofan.insdownloader.utils.HttpCacheUtils;


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

    class FetchInsTask extends AsyncTask<Void,Void,Boolean> {
        public static final int BUFFER_SIZE = 1024;
        @Override
        protected Boolean doInBackground(Void... params) {
            String url =  Parser.parseUrl(photoUrl);
            String fileName = url.substring(url.lastIndexOf("/") + 1);
            InputStream inputStream = null;
            FileOutputStream fos = null;
            try {
                URL url1 = new URL(url);
                inputStream = url1.openStream();
                File f = HttpCacheUtils.getDiskCacheDir(UserConfirmActivity.this,fileName);
                fos = new FileOutputStream(f);
                int read = 0;
                byte bytes[] = new byte[BUFFER_SIZE];
                while ((read = inputStream.read(bytes)) != -1){
                    fos.write(bytes,0,read);
                }
                fos.flush();
                return true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(inputStream != null){
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(fos != null){
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            super.onPostExecute(isSuccess);
            if(isSuccess){
                Toast.makeText(UserConfirmActivity.this,"恭喜，图片下载成功",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(UserConfirmActivity.this,"图片下载失败",Toast.LENGTH_LONG).show();
            }
            finish();
        }
    }

}
