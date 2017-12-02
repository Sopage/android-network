package android.network.binder.remote;

import android.network.binder.remote.IRemoteCallback;
import android.network.protocol.Message;

interface IRemoteBinder {

    boolean register(IRemoteCallback cb);

    boolean unregister(IRemoteCallback cb);

    void login(int uid, String token);

    void logout();

    boolean send(in Message message);

}
