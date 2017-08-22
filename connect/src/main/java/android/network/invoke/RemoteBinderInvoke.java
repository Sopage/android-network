package android.network.invoke;

import android.network.binder.ICallback;
import android.network.remote.binder.IRemoteBinder;
import android.os.RemoteCallbackList;
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

    public static boolean start(IRemoteBinder remote) {
        if (remote != null) {
            try {
                remote.start();
                return true;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean stop(IRemoteBinder remote) {
        if (remote != null) {
            try {
                remote.stop();
                return true;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
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

    public static void onStatusCallback(RemoteCallbackList<ICallback> callbackList, int status) {
        int n = callbackList.beginBroadcast();
        for (int i = 0; i < n; i++) {
            try {
                callbackList.getBroadcastItem(i).onStatus(status);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        callbackList.finishBroadcast();
    }

    public static void onMessageCallback(RemoteCallbackList<ICallback> callbackList,byte[] body) {
        int n = callbackList.beginBroadcast();
        for (int i = 0; i < n; i++) {
            try {
                callbackList.getBroadcastItem(i).onMessage(body);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        callbackList.finishBroadcast();
    }

}
