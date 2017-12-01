package android.network.sdk;

import android.network.binder.remote.IRemoteCallback;
import android.network.protocol.Body;
import android.network.sdk.listener.OnReceiverMessage;
import android.os.Handler;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Mr.Huang
 * @date 2017/8/21
 */
public class Receiver extends IRemoteCallback.Stub {

    private List<OnReceiverMessage> receiverMessageList = new ArrayList<>();
    private Handler handler;

    public Receiver(Handler handler) {
        this.handler = handler;
    }

    public void register(OnReceiverMessage receiverMessage) {
        receiverMessageList.add(receiverMessage);
    }

    public void unregister(OnReceiverMessage receiverMessage) {
        receiverMessageList.remove(receiverMessage);
    }

    @Override
    public final void onMessage(final Body body) throws RemoteException {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Iterator<OnReceiverMessage> iterator = receiverMessageList.iterator();
                while (iterator.hasNext()) {
                    iterator.next().onMessage(body);
                }
            }
        });
    }

    @Override
    public final void onStatus(final int status) throws RemoteException {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Iterator<OnReceiverMessage> iterator = receiverMessageList.iterator();
                while (iterator.hasNext()) {
                    iterator.next().onStatus(status);
                }
            }
        });
    }
}
