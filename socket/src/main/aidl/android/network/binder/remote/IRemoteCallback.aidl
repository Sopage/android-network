package android.network.binder.remote;

interface IRemoteCallback {

    void onMessage(inout byte[] body);

    void onStatus(int status);

}
