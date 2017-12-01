package android.network.sdk;

import android.network.invoke.RemoteBinderInvoke;
import android.network.protocol.MessageBody;
import android.network.remote.RemoteServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;

/**
 * @author Mr.Huang
 * @date 2017/8/21
 */
public class SenderManager {

    private RemoteServiceConnection mConnection;
    private HandlerThread mHandlerThread = new HandlerThread("invoke remote binder");
    private Handler mHandler;

    public SenderManager(RemoteServiceConnection connection) {
        this.mConnection = connection;
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
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
