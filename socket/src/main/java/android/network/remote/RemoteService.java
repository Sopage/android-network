package android.network.remote;

import android.app.Service;
import android.content.Intent;
import android.network.binder.remote.IRemoteBinder;
import android.network.binder.remote.IRemoteCallback;
import android.network.invoke.RemoteBinderInvoke;
import android.network.model.Status;
import android.network.protocol.Message;
import android.network.remote.codec.MessageCodec;
import android.network.remote.logger.DreamSocketLogger;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.dream.socket.DreamSocket;
import com.dream.socket.DreamTCPSocket;
import com.dream.socket.codec.MessageHandle;

/**
 * @author Mr.Huang
 * @date 2017/8/16
 */
public class RemoteService extends Service {

    private RemoteBinder binder = new RemoteBinder();
    private DreamSocket socket;
    private int uid = -1;
    private String token;

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("ESA", "onBind");
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("ESA", "onStartCommand");
        if (intent != null) {
            int uid = intent.getIntExtra("uid", -1);
            if (uid > 0) {
                this.uid = uid;
                token = intent.getStringExtra("token");
            }
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e("ESA", "onCreate");
        socket = new DreamTCPSocket("10.0.2.2", 6969);
        socket.setLogger(new DreamSocketLogger());
        socket.codec(new MessageCodec());
        socket.handle(handle);
    }

    private MessageHandle<Message> handle = new MessageHandle<Message>() {
        @Override
        public void onStatus(int status) {
            if (binder != null) {
                binder.onStatusCallback(status);
            }
            switch (status) {
                case Status.CONNECTED:
                    if (uid > 0) {
                        socket.send(getLoginBody(token));
                    }
                    break;
                case Status.DISCONNECTED:
                    break;
                case Status.FAIL:
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onMessage(Message message) {
            if (binder != null) {
                binder.onMessageCallback(message);
            }
        }
    };

    @Override
    public void onDestroy() {
        if (socket != null && socket.isConnected()) {
            socket.stop();
        }
        Intent intent = new Intent(this, RemoteService.class);
        if (uid > 0) {
            intent.putExtra("uid", uid);
            intent.putExtra("token", token);
        }
        startService(intent);
    }

    private final class RemoteBinder extends IRemoteBinder.Stub {

        private final RemoteCallbackList<IRemoteCallback> callbackList = new RemoteCallbackList<>();

        @Override
        public boolean register(IRemoteCallback cb) throws RemoteException {
            return callbackList.register(cb);
        }

        @Override
        public boolean unregister(IRemoteCallback cb) throws RemoteException {
            return callbackList.unregister(cb);
        }

        @Override
        public void login(int uid, String token) throws RemoteException {
            RemoteService.this.uid = uid;
            RemoteService.this.token = token;
            if (socket != null) {
                if (!socket.isConnected()) {
                    socket.start();
                } else {
                    send(getLoginBody(token));
                }
            }
        }

        @Override
        public void logout() throws RemoteException {
            uid = -1;
            token = null;
            if (socket != null && socket.isConnected()) {
                socket.stop();
            }
        }

        @Override
        public boolean send(Message message) throws RemoteException {
            if (socket != null && socket.isConnected()) {
                return socket.send(message);
            }
            return false;
        }

        public void onStatusCallback(int status) {
            RemoteBinderInvoke.onStatusCallback(callbackList, status);
        }

        public void onMessageCallback(Message message) {
            RemoteBinderInvoke.onMessageCallback(callbackList, message);
        }

    }

    private Message getLoginBody(final String token) {
        return new Message("", 0, uid, -1, token.getBytes());
    }

}
