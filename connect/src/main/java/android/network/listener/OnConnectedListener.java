package android.network.listener;

import android.network.binder.DataBinder;

/**
 * @author Mr.Huang
 * @date 2017/8/22
 */
public interface OnConnectedListener {

    void onConnected(DataBinder binder);

    void onDisconnect();

}
