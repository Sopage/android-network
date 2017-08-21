package android.network.binder;

import android.network.model.Status;
import android.os.RemoteException;

import com.dream.socket.codec.Handle;

/**
 * @author Mr.Huang
 * @date 2017/8/21
 */
public class CallbackBinder extends ICallback.Stub {

    private AbstractBinder binder;

    public CallbackBinder(AbstractBinder binder) {
        this.binder = binder;
    }

    @Override
    public void onMessage(byte[] body) throws RemoteException {
        if(binder != null && body != null){
            binder.handlerMessage(body);
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
                s = Status.DISCONNEDT;
                break;
            case Handle.STATUS_FAIL:
                s = Status.FAIL;
                break;
            default:
                s = Status.FAIL;
                break;
        }
        if(binder != null){
            binder.handlerStatus(s);
        }
    }
}
