package android.network.socket.binder;

import android.network.binder.IBinder;
import android.network.binder.ICallback;
import android.network.binder.IMessage;
import android.network.binder.TextMessage;
import android.network.socket.protobuf.BuildPacket;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.dream.socket.DreamSocket;

/**
 * @author Mr.Huang
 * @date 2017/8/17
 */

public class ServiceBinder extends IBinder.Stub {

    private final RemoteCallbackList<ICallback> callbackList = new RemoteCallbackList<>();
    private DreamSocket socket;
    private int uid;
    private String token;

    public void setSocket(DreamSocket socket) {
        this.socket = socket;
    }

    @Override
    public void register(ICallback cb) throws RemoteException {
        callbackList.register(cb);
    }

    @Override
    public void unregister(ICallback cb) throws RemoteException {
        callbackList.unregister(cb);
    }

    @Override
    public void login(int uid, String token) throws RemoteException {
        this.uid = uid;
        this.token = token;
        if (socket != null) {
            if (socket.isConnected()) {
                socket.stop();
            }
            socket.start();
        }
    }

    @Override
    public void logout() throws RemoteException {
        if (socket != null) {
            socket.send(BuildPacket.buildLogout(uid));
            socket.stop();
        }
    }

    @Override
    public void send(IMessage message) throws RemoteException {
        System.out.println(message instanceof TextMessage);
    }

    public void doLogin() {
        socket.send(BuildPacket.buildLogin(uid, token));
    }

    public void callback() {

    }

}
