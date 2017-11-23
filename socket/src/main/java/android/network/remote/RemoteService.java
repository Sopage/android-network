package android.network.remote;

import android.app.Service;
import android.content.Intent;
import android.network.protocol.Packet;
import android.network.remote.binder.RemoteBinder;
import android.network.remote.codec.MessageCodec;
import android.network.remote.logger.DreamSocketLogger;
import android.os.IBinder;

import com.dream.socket.DreamTCPSocket;
import com.dream.socket.codec.MessageHandle;

/**
 * @author Mr.Huang
 * @date 2017/8/16
 */
public class RemoteService extends Service implements RemoteBinder.OnRemoteMethodInvokeCallback {

    private RemoteBinder binder = new RemoteBinder();
    private DreamTCPSocket socket;

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
        socket = new DreamTCPSocket("192.168.31.43", 6969);
        socket.setLogger(new DreamSocketLogger());
        MessageCodec codec = new MessageCodec();
        socket.codec(codec.getDecode(), handle, codec.getEncode());
    }

    private MessageHandle<Packet> handle = new MessageHandle<Packet>() {
        @Override
        public void onStatus(int status) {
            if (binder != null) {
                binder.onStatusCallback(status);
            }
        }

        @Override
        public void onMessage(Packet data) {
            if (binder != null) {
                binder.onMessageCallback(data.getBody());
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
    public boolean send(byte[] array) {
        if (socket != null) {
            socket.send(new Packet(array));
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
