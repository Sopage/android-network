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
    private LocalCallback cb = new LocalCallback();

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        binder = ILocalBinder.Stub.asInterface(service);
        try {
            binder.register(cb);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        binder = null;
    }

    public LocalCallback getLocalCallback() {
        return cb;
    }

    public synchronized ILocalBinder getBinder() {
        return binder;
    }
}
