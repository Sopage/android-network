package android.network.remote.binder;

import android.network.binder.remote.IRemoteBinder;
import android.network.binder.remote.IRemoteCallback;
import android.network.invoke.RemoteBinderInvoke;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

/**
 * @author Mr.Huang
 * @date 2017/8/17
 */
public class RemoteBinder extends IRemoteBinder.Stub {

    private final RemoteCallbackList<IRemoteCallback> callbackList = new RemoteCallbackList<>();
    private OnRemoteMethodInvokeCallback callback;

    public void setOnRemoteMethodInvokeCallback(OnRemoteMethodInvokeCallback callback) {
        this.callback = callback;
    }

    @Override
    public boolean register(IRemoteCallback cb) throws RemoteException {
        return callbackList.register(cb);
    }

    @Override
    public boolean unregister(IRemoteCallback cb) throws RemoteException {
        return callbackList.unregister(cb);
    }

    @Override
    public void start() throws RemoteException {
        if (callback != null) {
            callback.start();
        }
    }

    @Override
    public void stop() throws RemoteException {
        if (callback != null) {
            callback.stop();
        }
    }

    @Override
    public boolean send(byte[] array) throws RemoteException {
        if (callback != null) {
            return callback.send(array);
        }
        return false;
    }

    public void onStatusCallback(int status) {
        RemoteBinderInvoke.onStatusCallback(callbackList, status);
    }

    public void onMessageCallback(byte[] body) {
        RemoteBinderInvoke.onMessageCallback(callbackList, body);
    }

    public interface OnRemoteMethodInvokeCallback {
        void start();

        void stop();

        boolean send(byte[] array);
    }

}
