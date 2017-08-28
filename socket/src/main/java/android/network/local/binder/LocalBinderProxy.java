package android.network.local.binder;

import android.network.protocol.protobuf.BodyType;
import android.network.protocol.protobuf.BuildPacket;
import android.network.protocol.protobuf.Protobuf;
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
        binder.loopInvokeSend(BuildPacket.buildLogin(uid, token).getBody());
        return true;
    }

    public boolean sendText(int receiver, int type, String text) {
        binder.loopInvokeSend(BuildPacket.buildTextMessage(uid, receiver, type, text).getBody());
        return true;
    }

    public boolean logout() throws RemoteException {
        binder.loopInvokeStop();
        return true;
    }

    public void onMessage(byte[] data) {
        try {
            Protobuf.Body body = Protobuf.Body.parseFrom(data);
            BodyType type = BodyType.getType(body.getType());
            switch (type) {
                case ACK:
                    break;
                case MESSAGE:
                    Protobuf.Message message = Protobuf.Message.parseFrom(body.getContent());
                    binder.onMessage(body.getSender(), message.getType(), message.getContent().toStringUtf8());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
