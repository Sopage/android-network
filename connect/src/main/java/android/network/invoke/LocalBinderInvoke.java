package android.network.invoke;

import android.network.local.LocalServiceConnection;
import android.os.RemoteException;

/**
 * @author Mr.Huang
 * @date 2017/8/23
 */
public abstract class LocalBinderInvoke {

    private LocalServiceConnection connection;

    public LocalBinderInvoke(LocalServiceConnection connection) {
        this.connection = connection;
    }

    public void invokeLogin(int uid, String token) {
        try {
            connection.getBinder().login(uid, token);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
