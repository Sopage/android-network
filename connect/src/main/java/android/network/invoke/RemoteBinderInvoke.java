package android.network.invoke;

import android.network.binder.remote.IRemoteCallback;
import android.network.binder.remote.IRemoteBinder;
import android.os.RemoteCallbackList;

/**
 * @author Mr.Huang
 * @date 2017/8/21
 */
public class RemoteBinderInvoke {

    public static boolean register(IRemoteBinder remote, IRemoteCallback cb) {
        try {
            if (remote != null && cb != null) {
                return remote.register(cb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean unregister(IRemoteBinder remote, IRemoteCallback cb) {
        try {
            if (remote != null && cb != null) {
                return remote.unregister(cb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean start(IRemoteBinder remote) {
        try {
            if (remote != null) {
                remote.start();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean stop(IRemoteBinder remote) {
        try {
            if (remote != null) {
                remote.stop();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean send(IRemoteBinder remote, byte[] data) {
        try {
            if (remote != null && data != null) {
                return remote.send(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void onStatusCallback(RemoteCallbackList<IRemoteCallback> callbackList, int status) {
        try {
            int n = callbackList.beginBroadcast();
            for (int i = 0; i < n; i++) {
                callbackList.getBroadcastItem(i).onStatus(status);
            }
            callbackList.finishBroadcast();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void onMessageCallback(RemoteCallbackList<IRemoteCallback> callbackList, byte[] body) {
        try {
            int n = callbackList.beginBroadcast();
            for (int i = 0; i < n; i++) {
                callbackList.getBroadcastItem(i).onMessage(body);
            }
            callbackList.finishBroadcast();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
