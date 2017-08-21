package android.network.sdk.connect;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.network.binder.DataBinder;
import android.os.IBinder;

/**
 * @author Mr.Huang
 * @date 2017/8/21
 */
public class DataServiceConnect implements ServiceConnection {

    private DataBinder binder;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        binder = (DataBinder) service;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        binder = null;
    }

    public DataBinder getBinder() {
        return binder;
    }
}
