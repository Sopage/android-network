package android.network.socket;

import android.app.Service;
import android.content.Intent;
import android.network.socket.binder.ServiceBinder;
import android.network.socket.codec.CodecHandle;
import android.os.IBinder;

import com.dream.socket.DreamSocket;

/**
 * @author Mr.Huang
 * @date 2017/8/16
 */
public class CoreService extends Service {

    private ServiceBinder binder = new ServiceBinder();
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
        socket = new DreamSocket();
        CodecHandle ch = new CodecHandle();
        ch.setBinder(binder);
        ch.setSocket(socket);
        binder.setSocket(socket);
        socket.setAddress("192.168.31.43", 6969);
        socket.setCodec(ch);
        socket.setHandle(ch);
    }

    @Override
    public void onDestroy() {
        if (socket != null && socket.isConnected()) {
            socket.stop();
        }
    }
}
