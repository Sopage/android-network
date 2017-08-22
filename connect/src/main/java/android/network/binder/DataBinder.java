package android.network.binder;

import android.network.listener.OnStatusListener;
import android.network.model.Status;
import android.network.protocol.protobuf.BuildPacket;


/**
 * @author Mr.Huang
 * @date 2017/8/21
 */
public class DataBinder extends AbstractBinder implements IDataBinder, OnStatusListener {

    private int uid;
    private String token;

    @Override
    public void login(int uid, String token) {
        removeOnStatusListener(this);
        addOnStatusListener(this);
        this.uid = uid;
        this.token = token;
        loopInvokeStart();
    }

    @Override
    public void logout() {
        loopInvokeSend(BuildPacket.buildLogout(1).getBody());
    }

    @Override
    public void onStatus(int status) {
        switch (status) {
            case Status.CONNECTED:
                loopInvokeSend(BuildPacket.buildLogin(uid, token).getBody());
                break;
        }
    }
}
