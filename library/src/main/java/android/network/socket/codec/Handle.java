package android.network.socket.codec;

public interface Handle<T> {

    int STATUS_CONNECTED = 0;

    int STATUS_DISCONNECT = 1;

    int STATUS_FAIL = 2;

    void onStatus(int status);

    void onReceive(Object object);

}
