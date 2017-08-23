package android.network.local;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.network.binder.local.ILocalBinder;
import android.os.IBinder;

/**
 * @author Mr.Huang
 * @date 2017/8/23
 */
public class LocalServiceConnection implements ServiceConnection {

    private ILocalBinder binder;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        binder = ILocalBinder.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        binder = null;
    }

    public synchronized ILocalBinder getBinder() {
        return binder;
    }
}
