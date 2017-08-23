package android.network.local.binder;

import android.network.protocol.protobuf.BuildPacket;
import android.os.RemoteException;

/**
 * @author Mr.Huang
 * @date 2017/8/23
 */
public class LocalBinderProxy {

    private LocalBinder binder;
    private int uid;
    private String token;

    public LocalBinderProxy(LocalBinder binder) {
        this.binder = binder;
    }

    public boolean login(int uid, String token) throws RemoteException {
        this.uid = uid;
        this.token = token;
        binder.loopInvokeStart();
        return true;
    }

    public boolean login(){
        if(binder.getRemote() != null){
            binder.loopInvokeSend(BuildPacket.buildLogin(uid, token).getBody());
        }
        return false;
    }

    public boolean logout() throws RemoteException {
        return false;
    }
}
