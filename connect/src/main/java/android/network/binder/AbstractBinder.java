package android.network.binder;

import android.network.invoke.LoopInvoke;
import android.network.invoke.RemoteBinderInvoke;
import android.network.listener.OnMessageListener;
import android.network.listener.OnStatusListener;
import android.network.remote.binder.IRemoteBinder;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Mr.Huang
 * @date 2017/8/21
 */
public abstract class AbstractBinder extends Binder {

    private HandlerThread ht = new HandlerThread("binder loop");
    private List<OnStatusListener> statusListeners = new ArrayList<>();
    private List<OnMessageListener> messageListeners = new ArrayList<>();
    private IRemoteBinder remote;
    private Handler handler;

    public AbstractBinder() {
        ht.start();
        handler = new Handler(ht.getLooper());
    }

    public void setRemoteBinder(IRemoteBinder remote) {
        this.remote = remote;
    }

    public void addOnStatusListener(OnStatusListener listener) {
        statusListeners.add(listener);
    }

    public void removeOnStatusListener(OnStatusListener listener) {
        statusListeners.remove(listener);
    }

    public void addOnMessageListener(OnMessageListener listener) {
        messageListeners.add(listener);
    }

    public void removeOnMessageListener(OnMessageListener listener) {
        messageListeners.remove(listener);
    }

    protected void handlerStatus(int status) {
        Iterator<OnStatusListener> iterator = statusListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onStatus(status);
        }
    }

    protected void handlerMessage(byte[] array) {
        Iterator<OnMessageListener> iterator = messageListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onMessage(array);
        }
    }

    protected void loopInvokeStart() {
        if (remote != null) {
            RemoteBinderInvoke.start(remote);
        } else {
            new LoopInvoke(handler) {
                @Override
                protected boolean invoke() {
                    if (remote != null) {
                        RemoteBinderInvoke.start(remote);
                        return true;
                    }
                    return true;
                }
            }.start();
        }
    }

    protected void loopInvokeStop() {
        if (remote != null) {
            RemoteBinderInvoke.stop(remote);
        } else {
            new LoopInvoke(handler) {
                @Override
                protected boolean invoke() {
                    if (remote != null) {
                        RemoteBinderInvoke.stop(remote);
                        return true;
                    }
                    return true;
                }
            }.start();
        }
    }

    protected void loopInvokeSend(final byte[] array) {
        if (remote != null) {
            RemoteBinderInvoke.send(remote, array);
        } else {
            new LoopInvoke(handler) {
                @Override
                protected boolean invoke() {
                    if (remote != null) {
                        RemoteBinderInvoke.send(remote, array);
                        return true;
                    }
                    return true;
                }
            }.start();
        }
    }
}
