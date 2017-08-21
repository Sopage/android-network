package android.network.binder;

import android.network.remote.binder.IRemoteBinder;
import android.os.RemoteException;

/**
 * @author Mr.Huang
 * @date 2017/8/21
 */
public class RemoteBinderInvoke {

    public static boolean register(IRemoteBinder remote, ICallback cb) {
        if (remote != null && cb != null) {
            try {
                return remote.register(cb);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean unregister(IRemoteBinder remote, ICallback cb) {
        if (remote != null && cb != null) {
            try {
                return remote.unregister(cb);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void start(IRemoteBinder remote) {
        if (remote != null) {
            try {
                remote.start();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void stop(IRemoteBinder remote) {
        if (remote != null) {
            try {
                remote.stop();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean send(IRemoteBinder remote, byte[] data) {
        if (remote != null && data != null) {
            try {
                return remote.send(data);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
