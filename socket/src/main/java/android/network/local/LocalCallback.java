package android.network.local;

import android.network.binder.local.ILocalCallback;
import android.network.listener.OnReceiverMessage;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Mr.Huang
 * @date 2017/8/23
 */
public class LocalCallback extends ILocalCallback.Stub {

    private List<OnReceiverMessage> onRMList = new ArrayList<>();

    @Override
    public void onMessage(int sender, int type, String text) throws RemoteException {
        Iterator<OnReceiverMessage> iterator = onRMList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onMessage(sender, type, text);
        }
    }

    public void addOnReceiverMessage(OnReceiverMessage onReceiverMessage) {
        onRMList.add(onReceiverMessage);
    }

}
