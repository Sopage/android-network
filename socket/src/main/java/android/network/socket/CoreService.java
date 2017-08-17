package android.network.socket;

import android.app.Service;
import android.content.Intent;
import android.network.binder.ICallback;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

/**
 * @author Mr.Huang
 * @date 2017/8/16
 */

public class CoreService extends Service {

    private ServiceBinder binder = new ServiceBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private static final class ServiceBinder extends android.network.binder.IBinder.Stub {

        private final RemoteCallbackList<ICallback> callbackList = new RemoteCallbackList<>();

        @Override
        public void register(ICallback cb) throws RemoteException {
            callbackList.register(cb);
        }

        @Override
        public void unregister(ICallback cb) throws RemoteException {
            callbackList.unregister(cb);
        }

        @Override
        public void login(int uid, String token) throws RemoteException {

        }

        @Override
        public void logout() throws RemoteException {

        }
    }
}
