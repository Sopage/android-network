package android.network.sdk;

import android.network.sdk.connect.DataServiceConnect;

/**
 * @author Mr.Huang
 * @date 2017/8/21
 */
public class MessageSender {

    private DataServiceConnect connect;

    public MessageSender(DataServiceConnect connect) {
        this.connect = connect;
    }

    public void login(int uid, String toke){
        connect.getBinder().login(uid, toke);
    }
}
