package android.network.binder;
import android.network.binder.ICallback;
interface IBinder {
    void register(ICallback cb);
    void unregister(ICallback cb);
    void login(int uid, String token);
    void logout();
}
