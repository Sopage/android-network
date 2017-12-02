package android.network.invoke;

import android.network.binder.remote.IRemoteCallback;
import android.network.protocol.Message;
import android.os.RemoteException;

/**
 *
 */
public class RemoteCallbackBinder extends IRemoteCallback.Stub {

    @Override
    public void onMessage(Message message) throws RemoteException {

    }

    @Override
    public void onStatus(int status) throws RemoteException {

    }
}
