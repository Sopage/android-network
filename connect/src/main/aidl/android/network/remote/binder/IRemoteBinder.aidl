package android.network.remote.binder;
import android.network.binder.ICallback;
interface IRemoteBinder {

    boolean register(ICallback cb);

    boolean unregister(ICallback cb);

    void start();

    void stop();

    boolean send(inout byte[] array);

}
