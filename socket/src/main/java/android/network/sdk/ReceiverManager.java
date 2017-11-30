package android.network.sdk;

import android.network.binder.remote.IRemoteCallback;
import android.network.invoke.RemoteBinderInvoke;
import android.network.protocol.Body;
import android.network.remote.RemoteServiceConnection;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;

/**
 * @author Mr.Huang
 * @date 2017/8/21
 */
public class ReceiverManager extends IRemoteCallback.Stub {

    private RemoteServiceConnection mConnection;
    private Handler mHandler;

    public ReceiverManager(RemoteServiceConnection connection) {
        this.mConnection = connection;
        mHandler = new Handler(Looper.getMainLooper());
        RemoteBinderInvoke.loopInvokeRegister(mHandler, connection, this);
    }

    @Override
    public void onMessage(Body body) throws RemoteException {

    }

    @Override
    public void onStatus(int status) throws RemoteException {

    }
}
