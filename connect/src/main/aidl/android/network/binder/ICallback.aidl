package android.network.binder;

interface ICallback {

    void onMessage(inout byte[] body);

    void onStatus(int status);

}
