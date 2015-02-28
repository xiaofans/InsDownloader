package xiaofan.insdownloader.events;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * Created by zhaoyu on 2014/10/18.
 */
public final class EventBus {
    private static Bus bus = new Bus();

    public static void register(final Object object){
        if(Looper.myLooper() == Looper.getMainLooper()){
            bus.register(object);
            return;
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                bus.register(object);
            }
        });
    }

    public static void unregister(final Object object){
        if(Looper.myLooper() == Looper.getMainLooper()){
             bus.unregister(object);
            return;
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                bus.unregister(object);
            }
        });
    }

    public static void post(final Object object){
        if(Looper.myLooper() == Looper.getMainLooper()){
            bus.post(object);
            return;
        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                bus.post(object);
            }
        });
    }


}
