package android.network.remote.binder;

import android.network.binder.ICallback;
import android.network.protocol.Packet;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.dream.socket.DreamSocket;

/**
 * @author Mr.Huang
 * @date 2017/8/17
 */
public class ServiceBinder extends IRemoteBinder.Stub {

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
        int n = callbackList.beginBroadcast();
        for (int i = 0; i < n; i++) {
            try {
                callbackList.getBroadcastItem(i).onStatus(status);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        callbackList.finishBroadcast();
    }

    public void onMessageCallback(byte[] body) {
        int n = callbackList.beginBroadcast();
        for (int i = 0; i < n; i++) {
            try {
                callbackList.getBroadcastItem(i).onMessage(body);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        callbackList.finishBroadcast();
    }

}
