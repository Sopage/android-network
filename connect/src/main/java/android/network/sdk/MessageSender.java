package android.network.sdk;

import android.network.binder.DataBinder;
import android.network.listener.OnConnectedListener;
import android.network.local.connect.DataServiceConnect;

/**
 * @author Mr.Huang
 * @date 2017/8/21
 */
public class MessageSender implements OnConnectedListener{

    private DataServiceConnect connect;
    private boolean isConnected = false;

    public MessageSender(DataServiceConnect connect) {
        this.connect = connect;
        this.connect.addOnConnectedListener(this);
    }

    public boolean login(int uid, String toke) {
        if(isConnected()){
            connect.getBinder().login(uid, toke);
            return true;
        }
        return false;
    }

    @Override
    public void onConnected(DataBinder binder) {
        this.connected(true);
    }

    @Override
    public void onDisconnect() {
        this.connected(false);
    }

    private synchronized boolean isConnected(){
        return isConnected;
    }

    private synchronized void connected(boolean isConnected){
        this.isConnected = isConnected;
    }
}
