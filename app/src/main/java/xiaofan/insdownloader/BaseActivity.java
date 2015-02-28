package xiaofan.insdownloader;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import xiaofan.insdownloader.events.EventBus;

/**
 * Created by zhaoyu on 2015/2/27.
 */
public class BaseActivity extends ActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.register(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.unregister(this);
    }
}
