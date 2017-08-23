package android.network.local.binder;

import android.network.binder.remote.IRemoteCallback;
import android.network.model.Status;
import android.os.RemoteException;

import com.dream.socket.codec.Handle;

/**
 * @author Mr.Huang
 * @date 2017/8/21
 */
public class RemoteCallbackBinder extends IRemoteCallback.Stub {

    private RemoteCallback callback;

    public RemoteCallbackBinder(RemoteCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onMessage(byte[] body) throws RemoteException {
        if(callback != null && body != null){
            callback.onMessage(body);
        }
    }

    @Override
    public void onStatus(int status) throws RemoteException {
        int s;
        switch (status){
            case Handle.STATUS_CONNECTED:
                s = Status.CONNECTED;
                break;
            case Handle.STATUS_DISCONNECT:
                s = Status.DISCONNECTED;
                break;
            case Handle.STATUS_FAIL:
                s = Status.FAIL;
                break;
            default:
                s = Status.FAIL;
                break;
        }
        if(callback != null){
            callback.onStatus(s);
        }
    }

    public interface RemoteCallback{
        void onStatus(int status);
        void onMessage(byte[] body);
    }
}
