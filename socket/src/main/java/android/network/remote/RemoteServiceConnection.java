package android.network.remote;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.network.binder.remote.IRemoteBinder;
import android.network.binder.remote.IRemoteCallback;
import android.network.invoke.RemoteBinderInvoke;
import android.os.Handler;
import android.os.IBinder;

/**
 *
 */
public class RemoteServiceConnection implements ServiceConnection {

    private IRemoteBinder mBinder;
    private IRemoteCallback mRemoteCallback;
    private Handler mHandler;

    public RemoteServiceConnection(IRemoteCallback callback, Handler handler){
        this.mRemoteCallback = callback;
        this.mHandler = handler;
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mBinder = IRemoteBinder.Stub.asInterface(iBinder);
        RemoteBinderInvoke.loopInvokeRegister(mHandler, this, mRemoteCallback);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mBinder = null;
    }

    public IRemoteBinder getBinder() {
        return mBinder;
    }
}
