package android.network.http.sample;

import android.app.Application;
import android.network.sdk.DreamManager;

/**
 * @author Mr.Huang
 * @date 2017/8/21
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DreamManager.register(this);
    }
}
