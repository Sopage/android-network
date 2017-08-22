package android.network.sdk;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.network.local.DataService;
import android.network.local.LocalService;
import android.network.local.connect.DataServiceConnect;

/**
 * @author Mr.Huang
 * @date 2017/8/21
 */
public class DreamManager {

    private static DataServiceConnect connect;
    private static MessageReceiver receiver;
    private static MessageSender sender;

    public static void register(Application application) {
        Intent ls = new Intent(application, LocalService.class);
        application.startService(ls);

        connect = new DataServiceConnect();
        sender = new MessageSender(connect);
        receiver = new MessageReceiver(connect);

        Intent ds = new Intent(application, DataService.class);
        application.startService(ds);
        application.bindService(ds, connect, Service.BIND_AUTO_CREATE);
    }

    public static void unregister(Application application){
        if(connect != null){
            application.unbindService(connect);
        }
    }

    public static MessageSender getSender() {
        return sender;
    }

    public static MessageReceiver getReceiver() {
        return receiver;
    }
}
