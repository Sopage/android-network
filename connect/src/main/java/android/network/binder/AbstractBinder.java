package android.network.binder;

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

    private static final int LOOP_TIME = 100;
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

    public void addOnStatusListener(OnStatusListener listener){
        statusListeners.add(listener);
    }

    public void removeOnStatusListener(OnStatusListener listener){
        statusListeners.remove(listener);
    }

    public void addOnMessageListener(OnMessageListener listener){
        messageListeners.add(listener);
    }

    public void removeOnMessageListener(OnMessageListener listener){
        messageListeners.remove(listener);
    }

    protected void handlerStatus(int status){
        Iterator<OnStatusListener> iterator = statusListeners.iterator();
        while (iterator.hasNext()){
            iterator.next().onStatus(status);
        }
    }

    protected void handlerMessage(byte[] array){
        Iterator<OnMessageListener> iterator = messageListeners.iterator();
        while (iterator.hasNext()){
            iterator.next().onMessage(array);
        }
    }

    protected void handlerInvokeStart() {
        if (remote != null) {
            RemoteBinderInvoke.start(remote);
            return;
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (remote == null) {
                    handler.postDelayed(this, LOOP_TIME);
                } else {
                    RemoteBinderInvoke.start(remote);
                }
            }
        }, LOOP_TIME);
    }

    protected void handlerInvokeSend(final byte[] array) {
        if (remote != null) {
            RemoteBinderInvoke.send(remote, array);
            return;
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (remote == null) {
                    handler.postDelayed(this, LOOP_TIME);
                } else {
                    RemoteBinderInvoke.send(remote, array);
                }
            }
        }, LOOP_TIME);
    }
}
