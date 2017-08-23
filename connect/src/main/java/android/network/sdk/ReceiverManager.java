package android.network.sdk;

import android.network.invoke.LocalBinderInvoke;
import android.network.local.LocalServiceConnection;

/**
 * @author Mr.Huang
 * @date 2017/8/21
 */
public class ReceiverManager extends LocalBinderInvoke {

    public ReceiverManager(LocalServiceConnection connection) {
        super(connection);
    }
}
