package android.network.binder.remote;

import android.network.binder.remote.IRemoteCallback;
import android.network.protocol.Body;

interface IRemoteBinder {

    boolean register(IRemoteCallback cb);

    boolean unregister(IRemoteCallback cb);

    void start();

    void stop();

    boolean send(in Body body);

}
