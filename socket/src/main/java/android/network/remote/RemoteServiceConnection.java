package android.network.remote;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.network.binder.remote.IRemoteBinder;
import android.os.IBinder;

/**
 *
 */
public class RemoteServiceConnection implements ServiceConnection {

    private IRemoteBinder mBinder;

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mBinder = IRemoteBinder.Stub.asInterface(iBinder);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mBinder = null;
    }

    public IRemoteBinder getBinder() {
        return mBinder;
    }
}
