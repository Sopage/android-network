package android.network.remote;

import android.app.Service;
import android.content.Intent;
import android.network.protocol.Body;
import android.network.remote.binder.RemoteBinder;
import android.network.remote.codec.MessageCodec;
import android.network.remote.logger.DreamSocketLogger;
import android.os.IBinder;
import android.util.Log;

import com.dream.socket.DreamSocket;
import com.dream.socket.DreamTCPSocket;
import com.dream.socket.codec.Message;
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

    private MessageHandle handle = new MessageHandle() {
        @Override
        public void onStatus(int status) {
            if (binder != null) {
                binder.onStatusCallback(status);
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
        Log.e("ESA", body.toString());
        if (socket != null) {
            socket.send(new Body());
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        if (socket != null && socket.isConnected()) {
            socket.stop();
        }
        Intent thisService = new Intent(this, RemoteService.class);
        startService(thisService);
    }
}
