package android.network.local.connect;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.network.binder.DataBinder;
import android.network.listener.OnConnectedListener;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Mr.Huang
 * @date 2017/8/21
 */
public class DataServiceConnect implements ServiceConnection {

    private List<OnConnectedListener> connectedListeners = new ArrayList<>();
    private DataBinder binder;

    public void addOnConnectedListener(OnConnectedListener listener) {
        connectedListeners.add(listener);
    }

    public void removeOnConnectedListener(OnConnectedListener listener) {
        connectedListeners.remove(listener);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        if(service instanceof  DataBinder){
            binder = (DataBinder) service;
            Iterator<OnConnectedListener> iterator = connectedListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onConnected(binder);
            }
        }

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Iterator<OnConnectedListener> iterator = connectedListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDisconnect();
        }
        binder = null;
    }

    public DataBinder getBinder() {
        return binder;
    }
}
