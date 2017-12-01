package android.network.sdk;

import android.network.invoke.RemoteBinderInvoke;
import android.network.sdk.body.MessageBody;
import android.network.remote.RemoteServiceConnection;
import android.os.Handler;

/**
 * @author Mr.Huang
 * @date 2017/8/21
 */
public class Sender {

    private RemoteServiceConnection mConnection;
    private Handler mHandler;

    public Sender(RemoteServiceConnection connection, Handler handler) {
        this.mConnection = connection;
        this.mHandler = handler;
    }

    public void login(int uid, String token) {
        RemoteBinderInvoke.loopInvokeLogin(mHandler, mConnection, uid, token);
    }

    public void logout() {
        RemoteBinderInvoke.loopInvokeLogout(mHandler, mConnection);
    }

    public void send(MessageBody body) {
        RemoteBinderInvoke.loopInvokeSend(mHandler, mConnection, body);
    }
}
