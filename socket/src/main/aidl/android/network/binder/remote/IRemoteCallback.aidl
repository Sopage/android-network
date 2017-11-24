package android.network.binder.remote;

interface IRemoteCallback {

    void onMessage(in int type, in byte[] body);

    void onStatus(int status);

}
