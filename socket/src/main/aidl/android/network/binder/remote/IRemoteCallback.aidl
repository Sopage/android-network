package android.network.binder.remote;

import android.network.protocol.Body;

interface IRemoteCallback {

    void onMessage(in Body body);

    void onStatus(int status);

}
