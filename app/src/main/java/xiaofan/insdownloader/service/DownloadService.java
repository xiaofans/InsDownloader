package xiaofan.insdownloader.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import xiaofan.insdownloader.events.AllEvents;
import xiaofan.insdownloader.events.EventBus;
import xiaofan.insdownloader.insparser.Parser;
import xiaofan.insdownloader.utils.HttpCacheUtils;


public class DownloadService extends IntentService {

    public static Intent newIntent(Context context,String downloadUrl){
        Intent intent = new Intent(context,DownloadService.class);
        intent.putExtra("url",downloadUrl);
        return intent;
    }

    public static final int BUFFER_SIZE = 1024;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String url  = intent.getStringExtra("url");
            download(url);
        }
    }

    private void download(String url) {
        boolean isSuccess = false;
        url = Parser.parseUrl(url);
        InputStream inputStream = null;
        FileOutputStream fos = null;
        File file = null;
        if(!TextUtils.isEmpty(url)){
            String fileName = url.substring(url.lastIndexOf("/") + 1);
            try {
                URL url1 = new URL(url);
                inputStream = url1.openStream();
                file = HttpCacheUtils.getDiskCacheDir(getApplicationContext(), fileName);
                fos = new FileOutputStream(file);
                int read = 0;
                byte bytes[] = new byte[BUFFER_SIZE];
                while ((read = inputStream.read(bytes)) != -1){
                    fos.write(bytes,0,read);
                }
                isSuccess = true;
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
        }
        if(isSuccess){
            EventBus.post(new AllEvents.DownloadSuccessEvent(file.getAbsolutePath()));
        }else{
            EventBus.post(new AllEvents.DownloadFailureEvent());
        }
    }
}
