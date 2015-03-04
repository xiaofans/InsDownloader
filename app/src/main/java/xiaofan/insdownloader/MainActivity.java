package xiaofan.insdownloader;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

import xiaofan.insdownloader.fragment.MyDownloadFragment;
import xiaofan.insdownloader.service.DaemonService;
import xiaofan.insdownloader.utils.HttpCacheUtils;
import xiaofan.insdownloader.utils.Utils;


public class MainActivity extends BaseActivity{

    private DrawerLayout drawerLayout;
    private ListView mDrawerList;
    private String[] mPlanetTitles;

    public Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpActionbar();
        setUpViews();
        setUpFragments();
        testService();
    }

    private void setUpFragments() {
        String downloadedPaths = HttpCacheUtils.getCachedPath(this) + File.separator;
        ArrayList<String> list = new ArrayList<String>();
        File f = new File(downloadedPaths);
        if(f.exists() && f.listFiles() != null && f.listFiles().length > 0){
            File[] files = f.listFiles();
            for(int i = files.length - 1; i >= 0;i--){
                list.add(files[i].getAbsolutePath());
            }
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, MyDownloadFragment.newInstance(list),"MyDownloadFragment").commit();
    }

    private void setUpActionbar() {
        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.action_bar);
        toolbar.setTitle("INS下载器");
        toolbar.setBackgroundColor(Color.argb(255,69,115,153));
        setSupportActionBar(toolbar);
    }

    private void setUpViews() {
        mPlanetTitles = getResources().getStringArray(R.array.drawer_items);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

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



    private void testService() {
        Intent intent = new Intent(this, DaemonService.class);
        startService(intent);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

    }

}
