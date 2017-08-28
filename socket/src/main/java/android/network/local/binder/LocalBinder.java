package android.network.local.binder;

import android.network.binder.local.ILocalBinder;
import android.network.binder.local.ILocalCallback;
import android.network.binder.remote.IRemoteBinder;
import android.network.invoke.LoopInvoke;
import android.network.invoke.RemoteBinderInvoke;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

/**
 * @author Mr.Huang
 * @date 2017/8/23
 */
public class LocalBinder extends ILocalBinder.Stub {

    private HandlerThread ht = new HandlerThread("binder loop");
    private Handler handler;
    private GetRemoteBinder remoteBinder;
    private LocalBinderProxy proxy;
    private RemoteCallbackList<ILocalCallback> callbackList = new RemoteCallbackList<>();

    public LocalBinder() {
        ht.start();
        handler = new Handler(ht.getLooper());
        proxy = new LocalBinderProxy(this);
    }

    public void setRemoteBinder(GetRemoteBinder remoteBinder) {
        this.remoteBinder = remoteBinder;
    }

    @Override
    public boolean register(ILocalCallback cb) {
        return callbackList.register(cb);
    }

    @Override
    public boolean unregister(ILocalCallback cb) {
        return callbackList.unregister(cb);
    }

    @Override
    public boolean login(int uid, String token) throws RemoteException {
        return proxy.login(uid, token);
    }

    public boolean login() {
        proxy.login();
        return true;
    }

    @Override
    public boolean logout() throws RemoteException {
        return proxy.logout();
    }

    @Override
    public boolean sendText(int receiver, int type, String text) {
        return proxy.sendText(receiver, type, text);
    }

    public void loopInvokeStart() {
        if (remoteBinder == null) {
            return;
        }
        if (!RemoteBinderInvoke.start(remoteBinder.getRemoteBinder())) {
            new LoopInvoke(handler) {
                @Override
                protected boolean invoke() {
                    return RemoteBinderInvoke.start(remoteBinder.getRemoteBinder());
                }
            }.start();
        }
    }

    public void loopInvokeStop() {
        if (remoteBinder == null) {
            return;
        }
        if (!RemoteBinderInvoke.stop(remoteBinder.getRemoteBinder())) {
            new LoopInvoke(handler) {
                @Override
                protected boolean invoke() {
                    return RemoteBinderInvoke.stop(remoteBinder.getRemoteBinder());
                }
            }.start();
        }
    }

    public void loopInvokeSend(final byte[] array) {
        if (remoteBinder == null) {
            return;
        }
        if (!RemoteBinderInvoke.send(remoteBinder.getRemoteBinder(), array)) {
            new LoopInvoke(handler) {
                @Override
                protected boolean invoke() {
                    return RemoteBinderInvoke.send(remoteBinder.getRemoteBinder(), array);
                }
            }.start();
        }
    }

    public void onMessage(byte[] body) {
        proxy.onMessage(body);
    }

    public void onMessage(int sender, int type, String text) {
        try {
            int n = callbackList.beginBroadcast();
            for (int i = 0; i < n; i++) {
                callbackList.getBroadcastItem(i).onMessage(sender, type, text);
            }
            callbackList.finishBroadcast();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface GetRemoteBinder {
        IRemoteBinder getRemoteBinder();
    }
}
