package android.network.remote.binder;

import android.network.binder.ICallback;
import android.network.invoke.RemoteBinderInvoke;
import android.network.protocol.Packet;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.dream.socket.DreamSocket;

/**
 * @author Mr.Huang
 * @date 2017/8/17
 */
public class RemoteBinder extends IRemoteBinder.Stub {

    private final RemoteCallbackList<ICallback> callbackList = new RemoteCallbackList<>();
    private DreamSocket socket;

    public void setSocket(DreamSocket socket) {
        this.socket = socket;
    }

    @Override
    public boolean register(ICallback cb) throws RemoteException {
        callbackList.register(cb);
        return true;
    }

    @Override
    public boolean unregister(ICallback cb) throws RemoteException {
        callbackList.unregister(cb);
        return true;
    }

    @Override
    public void start() throws RemoteException {
        if (socket != null) {
            if (socket.isConnected()) {
                socket.stop();
            }
            socket.start();
        }
    }

    @Override
    public void stop() throws RemoteException {
        if (socket != null) {
            socket.stop();
        }
    }

    @Override
    public boolean send(byte[] array) throws RemoteException {
        if (socket.isConnected()) {
            socket.send(new Packet(array));
            return true;
        }
        return false;
    }

    public void onStatusCallback(int status) {
        RemoteBinderInvoke.onStatusCallback(callbackList, status);
    }

    public void onMessageCallback(byte[] body) {
        RemoteBinderInvoke.onMessageCallback(callbackList, body);
    }

}
