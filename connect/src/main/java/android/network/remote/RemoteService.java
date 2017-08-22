package android.network.remote;

import android.app.Service;
import android.content.Intent;
import android.network.remote.binder.RemoteBinder;
import android.network.remote.codec.CodecHandle;
import android.os.IBinder;
import android.util.Log;

import com.dream.socket.DreamSocket;
import com.dream.socket.config.Config;
import com.dream.socket.logger.Logger;

/**
 * @author Mr.Huang
 * @date 2017/8/16
 */
public class RemoteService extends Service {

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
        Config.getConfig().setLogger(new Logger() {
            @Override
            public void debug(String log) {
                Log.d("ESA", log);
            }

            @Override
            public void info(String log) {
                Log.i("ESA", log);
            }

            @Override
            public void warn(String log) {
                Log.w("ESA", log);
            }

            @Override
            public void warn(String log, Throwable throwable) {
                Log.w("ESA", log);
            }

            @Override
            public void error(String log) {
                Log.e("ESA", log);
            }

            @Override
            public void error(String log, Throwable throwable) {
                Log.e("ESA", log);
            }
        });
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
