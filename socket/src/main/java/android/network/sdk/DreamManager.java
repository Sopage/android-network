package android.network.sdk;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.network.remote.RemoteService;
import android.network.remote.RemoteServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * @author Mr.Huang
 * @date 2017/8/21
 */
public class DreamManager {

    private static HandlerThread mHandlerThread = new HandlerThread("invoke remote binder");
    private static RemoteServiceConnection mRemoteServiceConnection;
    private static Receiver mReceiver;
    private static Sender mSender;

    public static void register(Application application) {
        mHandlerThread.start();
        Handler handler = new Handler(mHandlerThread.getLooper());
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mReceiver = new Receiver(mainHandler);
        mRemoteServiceConnection = new RemoteServiceConnection(mReceiver, handler);
        mSender = new Sender(mRemoteServiceConnection, handler);

        Intent service = new Intent(application, RemoteService.class);
        application.startService(service);
        application.bindService(service, mRemoteServiceConnection, Service.BIND_AUTO_CREATE);
    }

    public static void unregister(Application application) {
        application.unbindService(mRemoteServiceConnection);
    }

    public static Sender getSender() {
        return mSender;
    }

    public static Receiver getReceiver() {
        return mReceiver;
    }


}
