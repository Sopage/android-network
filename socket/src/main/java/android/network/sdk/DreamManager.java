package android.network.sdk;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.network.remote.RemoteService;
import android.network.remote.RemoteServiceConnection;

/**
 * @author Mr.Huang
 * @date 2017/8/21
 */
public class DreamManager {

    private static RemoteServiceConnection connection = new RemoteServiceConnection();
    private static ReceiverManager receiver;
    private static SenderManager sender;

    public static void register(Application application) {
        sender = new SenderManager(connection);
        receiver = new ReceiverManager(connection);
        Intent service = new Intent(application, RemoteService.class);
        application.startService(service);
        application.bindService(service, connection, Service.BIND_AUTO_CREATE);
    }

    public static void unregister(Application application) {
        if (connection != null) {
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
