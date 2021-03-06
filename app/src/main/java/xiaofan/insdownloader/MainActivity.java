package xiaofan.insdownloader;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import xiaofan.insdownloader.fragment.AboutFragment;
import xiaofan.insdownloader.fragment.MyDownloadFragment;
import xiaofan.insdownloader.fragment.MyPicDownloadFragment;
import xiaofan.insdownloader.fragment.UseGuideFragment;
import xiaofan.insdownloader.service.MDaemonService;
import xiaofan.insdownloader.utils.HttpCacheUtils;


public class MainActivity extends BaseActivity implements View.OnClickListener{

    private DrawerLayout drawerLayout;
    private String[] mPlanetTitles;

    public Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;

    private TextView myDownloadsTv;
    private TextView useGuideTv;
    private TextView aboutTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpActionbar();
        setUpViews();
        setUpFragments();
        startService(new Intent(MainActivity.this, MDaemonService.class));
    }

    private void setUpFragments() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, MyDownloadFragment
            .newInstance(),"MyDownloadFragment").commit();
    }



    private void setUpActionbar() {
        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setBackgroundColor(Color.argb(255,69,115,153));
        setSupportActionBar(toolbar);
    }

    private void setUpViews() {
        myDownloadsTv = (TextView) findViewById(R.id.tv_my_downloads);
        myDownloadsTv.setOnClickListener(this);

        useGuideTv = (TextView) findViewById(R.id.tv_use_guide);
        useGuideTv.setOnClickListener(this);

        aboutTv = (TextView) findViewById(R.id.tv_about);
        aboutTv.setOnClickListener(this);

        mPlanetTitles = getResources().getStringArray(R.array.drawer_items);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ){

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();
    }


    @Override
    public void onClick(View v) {
        drawerLayout.closeDrawers();
        switch (v.getId()){
            case R.id.tv_my_downloads:
                toolbar.setTitle(getResources().getString(R.string.my_download));
                setUpFragments();
                break;
            case R.id.tv_use_guide:
                toolbar.setTitle(getResources().getString(R.string.get_started));
                setUpGuideFragment();
                break;
            case R.id.tv_about:
                toolbar.setTitle(getResources().getString(R.string.about));
                setUpAboutFragment();
                break;
        }
    }

    private void setUpGuideFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, UseGuideFragment.newInstance(),"UseGuideFragment").commit();
    }

    private void setUpAboutFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, AboutFragment.newInstance(),"AboutFragment").commit();
    }

}
