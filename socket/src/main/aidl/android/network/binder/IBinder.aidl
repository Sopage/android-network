package android.network.binder;
import android.network.binder.ICallback;
import android.network.binder.IMessage;
interface IBinder {
    void register(ICallback cb);
    void unregister(ICallback cb);
    void login(int uid, String token);
    void logout();
    void send(in IMessage message);
}
