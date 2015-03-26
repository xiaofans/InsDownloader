package xiaofan.insdownloader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import java.io.File;

import at.markushi.ui.CircleButton;
import xiaofan.insdownloader.utils.Utils;
import xiaofan.insdownloader.view.PanningBackgroundFrameLayout;


public class PhotoViewActivity extends BaseActivity implements View.OnClickListener{

    public static Intent newIntent(Context context,String filePath){
        Intent intent = new Intent(context,PhotoViewActivity.class);
        intent.putExtra("filePath",filePath);
        return intent;
    }

    private PanningBackgroundFrameLayout panningBackgroundFrameLayout;
    private String filePath;
    private CircleButton circleButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        filePath = getIntent().getStringExtra("filePath");
        setUpViews();
        Utils.fitScreenIfNeeded(this);
        Utils.addSystemUIPadding(this,panningBackgroundFrameLayout);
    }

    private void setUpViews() {
        circleButton = (CircleButton) findViewById(R.id.btn_share);
        circleButton.setOnClickListener(this);
        panningBackgroundFrameLayout = (PanningBackgroundFrameLayout) findViewById(R.id.panning_backround_bg);
        panningBackgroundFrameLayout.setClickToZoomEnabled(true);
        panningBackgroundFrameLayout.setPanningEnabled(true);
        panningBackgroundFrameLayout.setShouldAnimateBackgroundChange(true);

            panningBackgroundFrameLayout.post(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap =  BitmapFactory.decodeFile(filePath);
                    panningBackgroundFrameLayout.setPanningBackground(bitmap);
                    applyShareButtonColor(bitmap);
                }
            });

    }

    private void applyShareButtonColor(Bitmap bitmap) {
        Palette.generateAsync(bitmap,new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                circleButton.setColor(palette.getVibrantColor(Color.argb(0,255,255,255)));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_share:
                shareAction();
                break;
        }
    }

    private void shareAction() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }
}
