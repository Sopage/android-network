package android.network.binder.remote;

import android.network.binder.remote.IRemoteCallback;

interface IRemoteBinder {

    boolean register(IRemoteCallback cb);

    boolean unregister(IRemoteCallback cb);

    void start();

    void stop();

    boolean send(inout byte[] array);

}
