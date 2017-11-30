package android.network.remote;

import android.app.Service;
import android.content.Intent;
import android.network.protocol.Body;
import android.network.remote.binder.RemoteBinder;
import android.network.remote.codec.MessageCodec;
import android.network.remote.logger.DreamSocketLogger;
import android.os.IBinder;

import com.dream.socket.DreamSocket;
import com.dream.socket.DreamTCPSocket;
import com.dream.socket.codec.MessageHandle;

/**
 * @author Mr.Huang
 * @date 2017/8/16
 */
public class RemoteService extends Service implements RemoteBinder.OnRemoteMethodInvokeCallback {

    private RemoteBinder binder = new RemoteBinder();
    private DreamSocket socket;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        binder.setOnRemoteMethodInvokeCallback(this);
        socket = new DreamTCPSocket("10.0.2.2", 6969);
        socket.setLogger(new DreamSocketLogger());
        socket.codec(new MessageCodec());
        socket.handle(handle);
    }

    private MessageHandle<Body> handle = new MessageHandle<Body>() {
        @Override
        public void onStatus(int status) {
            if (binder != null) {
                binder.onStatusCallback(status);
            }
        }

        @Override
        public void onMessage(Body message) {
            if (binder != null) {
                binder.onMessageCallback(message);
            }
        }
    };

    @Override
    public void start() {
        if (socket != null && !socket.isConnected()) {
            socket.start();
        }
    }

    @Override
    public void stop() {
        if (socket != null && socket.isConnected()) {
            socket.stop();
        }
    }

    @Override
    public boolean send(Body body) {
        if (socket != null) {
            socket.send(body);
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        if (socket != null && socket.isConnected()) {
            socket.stop();
        }
        startService(new Intent(this, RemoteService.class));
    }
}
