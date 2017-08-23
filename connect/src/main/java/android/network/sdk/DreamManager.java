package android.network.sdk;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.network.local.LocalService;
import android.network.local.LocalServiceConnection;

/**
 * @author Mr.Huang
 * @date 2017/8/21
 */
public class DreamManager {

    private static LocalServiceConnection connection = new LocalServiceConnection();
    private static ReceiverManager receiver;
    private static SenderManager sender;

    public static void register(Application application) {
        sender = new SenderManager(connection);
        receiver = new ReceiverManager(connection);

        Intent ds = new Intent(application, LocalService.class);
        application.startService(ds);
        application.bindService(ds, connection, Service.BIND_AUTO_CREATE);
    }

    public static void unregister(Application application) {
        if(connection != null){
            application.unbindService(connection);
        }
    }

    public static SenderManager getSender() {
        return sender;
    }

    public static ReceiverManager getReceiver() {
        return receiver;
    }
}
