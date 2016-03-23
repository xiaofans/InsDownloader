package xiaofan.insdownloader.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.net.URLConnection;
import xiaofan.insdownloader.events.AllEvents;
import xiaofan.insdownloader.events.EventBus;
import xiaofan.insdownloader.insparser.MediaData;
import xiaofan.insdownloader.insparser.Parser;
import xiaofan.insdownloader.utils.HttpCacheUtils;


public class DownloadService extends IntentService {

    public static Intent newIntent(Context context,String downloadUrl){
        Intent intent = new Intent(context,DownloadService.class);
        intent.putExtra("url",downloadUrl);
        return intent;
    }

    public static final int BUFFER_SIZE = 1024 * 4;

    private long mContentLength;
    private long mCurrentBytes;

    private String actualUrl;
    private File downloadPath;
    private MediaData mediaData;


    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String url  = intent.getStringExtra("url");
            // step1. 计算出真实地址
            EventBus.post(new AllEvents.DownloadStatusEvent(AllEvents.DownloadStatusEvent.STATE_PARSE_URL));
            mediaData = Parser.parseMedia(url); // TODO 做失败处理
            actualUrl = mediaData.url;

            EventBus.post(new AllEvents.DownloadStatusEvent(AllEvents.DownloadStatusEvent.STATE_DOWNLOADING));
            // step2.计算出下载路径
            String fileName = actualUrl.substring(actualUrl.lastIndexOf("/") + 1);
            downloadPath = HttpCacheUtils.getDiskCacheDir(getApplicationContext(), fileName);

            // step3 开始下载
            executeDownload();
        }
    }

    private void executeDownload() {
        InputStream in = null;
        OutputStream out = null;
        FileDescriptor outFd = null;
        HttpURLConnection connection = null;
        cleanupDestination();
        try {
            connection = (HttpURLConnection) new URL(actualUrl).openConnection();
            readResponseHeaders(connection);
            in = connection.getInputStream();
            out = new FileOutputStream(downloadPath, true);
            outFd = ((FileOutputStream) out).getFD();
            transferData(in, out);
        }catch (IOException e){
            updateDownloadFailed();
            e.printStackTrace();
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (out != null) out.flush();
                if (outFd != null) outFd.sync();
            } catch (IOException e) {
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private int readResponseHeaders(HttpURLConnection conn) {
        final String transferEncoding = conn.getHeaderField("Transfer-Encoding");
        if (transferEncoding == null) {
            mContentLength = getHeaderFieldLong(conn, "Content-Length", -1);
        } else {
            mContentLength = -1;
        }

        if( mContentLength == -1
            && (transferEncoding == null || !transferEncoding.equalsIgnoreCase("chunked")) ) {
            return -1;
        } else {
            return 1;
        }
    }


    public long getHeaderFieldLong(URLConnection conn, String field, long defaultValue) {
        try {
            return Long.parseLong(conn.getHeaderField(field));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private void transferData(InputStream in, OutputStream out) {
        final byte data[] = new byte[BUFFER_SIZE];
        mCurrentBytes = 0;
        for (;;) {
            int bytesRead = readFromResponse( data, in);
            if (mContentLength != -1 && mContentLength > 0) {
                int progress = (int) ((mCurrentBytes * 100) / mContentLength);
                updateDownloadProgress(progress, mCurrentBytes);
            }
            if (bytesRead == -1) { // success, end of stream already reached
                updateDownloadComplete();
                return;
            } else if (bytesRead == Integer.MIN_VALUE) {
                return;
            }
            writeDataToDestination(data, bytesRead, out);
            mCurrentBytes += bytesRead;
        }
    }

    private void writeDataToDestination(byte[] data, int bytesRead, OutputStream out) {
        while (true) {
            try {
                out.write(data, 0, bytesRead);
                return;
            } catch (IOException ex) {
                ex.printStackTrace();
                updateDownloadFailed();
            }
        }
    }

    private void updateDownloadComplete() {
        EventBus.post(new AllEvents.DownloadSuccessEvent(downloadPath.getAbsolutePath(),!mediaData.isImage));
    }

    private void updateDownloadFailed() {
        EventBus.post(new AllEvents.DownloadFailureEvent());
    }

    public void updateDownloadProgress(int progress, long downloadedBytes) {
        ProgressInfo progressInfo = new ProgressInfo(progress,mContentLength,downloadedBytes);
        EventBus.post(new AllEvents.DownloadStatusEvent(
            AllEvents.DownloadStatusEvent.STATE_DOWNLOADING,progressInfo));
    }

    private int readFromResponse( byte[] data, InputStream entityStream) {
        try {
            return entityStream.read(data);
        } catch (IOException ex) {
            if ("unexpected end of stream".equals(ex.getMessage())) {
                return -1;
            }
            return Integer.MIN_VALUE;
        }
    }

    private void cleanupDestination() {
        if(downloadPath != null && downloadPath.exists()){
            downloadPath.delete();
        }
    }


    private void download(String url) {
        boolean isSuccess = false;
        EventBus.post(new AllEvents.DownloadStatusEvent(AllEvents.DownloadStatusEvent.STATE_PARSE_URL));
        url = Parser.parseUrl(url);
        EventBus.post(new AllEvents.DownloadStatusEvent(AllEvents.DownloadStatusEvent.STATE_DOWNLOADING));
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
            //EventBus.post(new AllEvents.DownloadSuccessEvent(file.getAbsolutePath()));
        }else{
            EventBus.post(new AllEvents.DownloadFailureEvent());
        }
    }

}
