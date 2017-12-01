package android.network.invoke;

import android.network.binder.remote.IRemoteBinder;
import android.network.binder.remote.IRemoteCallback;
import android.network.protocol.Body;
import android.network.protocol.MessageBody;
import android.network.remote.RemoteServiceConnection;
import android.os.Handler;
import android.os.RemoteCallbackList;

/**
 * @author Mr.Huang
 * @date 2017/8/21
 */
public class RemoteBinderInvoke {

    public static void loopInvokeRegister(Handler handler, final RemoteServiceConnection connection, final IRemoteCallback callback) {
        if (!RemoteBinderInvoke.register(connection.getBinder(), callback)) {
            new LoopInvoke(handler) {
                @Override
                protected boolean invoke() {
                    return RemoteBinderInvoke.register(connection.getBinder(), callback);
                }
            }.start();
        }
    }

    public static void loopInvokeLogin(Handler handler, final RemoteServiceConnection connection, final int uid, final String token) {
        if (!RemoteBinderInvoke.login(connection.getBinder(), uid, token)) {
            new LoopInvoke(handler) {
                @Override
                protected boolean invoke() {
                    return RemoteBinderInvoke.login(connection.getBinder(), uid, token);
                }
            }.start();
        }
    }

    public static void loopInvokeLogout(Handler handler, final RemoteServiceConnection connection) {
        if (!RemoteBinderInvoke.logout(connection.getBinder())) {
            new LoopInvoke(handler) {
                @Override
                protected boolean invoke() {
                    return RemoteBinderInvoke.logout(connection.getBinder());
                }
            }.start();
        }
    }

    public static void loopInvokeSend(Handler handler, final RemoteServiceConnection connection, final MessageBody body) {
        if (!RemoteBinderInvoke.send(connection.getBinder(), body)) {
            new LoopInvoke(handler) {
                @Override
                protected boolean invoke() {
                    return RemoteBinderInvoke.send(connection.getBinder(), body);
                }
            }.start();
        }
    }

    private static boolean register(IRemoteBinder remote, IRemoteCallback cb) {
        try {
            if (remote != null && cb != null) {
                return remote.register(cb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean unregister(IRemoteBinder remote, IRemoteCallback cb) {
        try {
            if (remote != null && cb != null) {
                return remote.unregister(cb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean login(IRemoteBinder remote, int uid, String token) {
        try {
            if (remote != null) {
                remote.login(uid, token);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean logout(IRemoteBinder remote) {
        try {
            if (remote != null) {
                remote.logout();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean send(IRemoteBinder remote, MessageBody body) {
        try {
            if (remote != null && body != null) {
                return remote.send(body);
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

    public static void onMessageCallback(RemoteCallbackList<IRemoteCallback> callbackList, Body body) {
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
