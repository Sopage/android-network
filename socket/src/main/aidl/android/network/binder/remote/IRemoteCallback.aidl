package android.network.binder.remote;

import android.network.protocol.Message;

interface IRemoteCallback {

    void onMessage(in Message message);

    void onStatus(int status);

}
