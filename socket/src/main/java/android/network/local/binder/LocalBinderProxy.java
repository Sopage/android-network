package android.network.local.binder;

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

    public boolean login() {
//        binder.loopInvokeSend(BuildPacket.buildLogin(uid, token).getBody());
        return true;
    }

    public boolean sendText(int receiver, int type, String text) {
        binder.loopInvokeSend(type, text.getBytes());
        return true;
    }

    public boolean logout() throws RemoteException {
        binder.loopInvokeStop();
        return true;
    }

    public void onMessage(byte[] data) {
        binder.onMessage(1,1, new String(data));
    }


}
