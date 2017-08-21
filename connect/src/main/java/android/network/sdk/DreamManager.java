package android.network.sdk;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.network.DataService;
import android.network.sdk.connect.DataServiceConnect;

/**
 * @author Mr.Huang
 * @date 2017/8/21
 */
public class DreamManager {

    private static DataServiceConnect connect;
    private static MessageReceiver receiver;
    private static MessageSender sender;

    public static void register(Application application) {
        Intent service = new Intent(application, DataService.class);
        application.startService(service);
        connect = new DataServiceConnect();
        application.bindService(service, connect, Service.BIND_AUTO_CREATE);
        receiver = new MessageReceiver(connect);
        sender = new MessageSender(connect);
    }

    public static void unregister(Application application){
        if(connect != null){
            application.unbindService(connect);
        }
        connect = null;
        receiver = null;
        sender = null;
    }

    public static MessageSender getSender() {
        return sender;
    }

    public static MessageReceiver getReceiver() {
        return receiver;
    }
}
