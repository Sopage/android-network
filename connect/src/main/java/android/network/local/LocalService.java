package android.network.local;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.network.binder.ICallback;
import android.network.invoke.LoopInvoke;
import android.network.invoke.RemoteBinderInvoke;
import android.network.remote.RemoteService;
import android.network.remote.binder.IRemoteBinder;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

/**
 * @author Mr.Huang
 * @date 2017/8/22
 */
public class LocalService extends Service implements ServiceConnection {

    private final RemoteCallbackList<ICallback> callbackList = new RemoteCallbackList<>();
    private LocalRemoteBuilder localRemote = new LocalRemoteBuilder();
    private Handler handler = new Handler();
    private IRemoteBinder remote;
    private LocalCallback cb;

    @Override
    public IBinder onBind(Intent intent) {
        return localRemote;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent remote = new Intent(this, RemoteService.class);
        bindService(remote, this, Service.BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        remote = IRemoteBinder.Stub.asInterface(service);
        cb = new LocalCallback();
        RemoteBinderInvoke.register(remote, cb);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        remote = null;
    }

    private class LocalCallback extends ICallback.Stub {

        @Override
        public void onMessage(byte[] body) throws RemoteException {
            RemoteBinderInvoke.onMessageCallback(callbackList, body);
        }

        @Override
        public void onStatus(int status) throws RemoteException {
            RemoteBinderInvoke.onStatusCallback(callbackList, status);
        }
    }

    private class LocalRemoteBuilder extends IRemoteBinder.Stub {
        @Override
        public boolean register(ICallback cb) throws RemoteException {
            return callbackList.register(cb);
        }

        @Override
        public boolean unregister(ICallback cb) throws RemoteException {
            return callbackList.unregister(cb);
        }

        @Override
        public void start() throws RemoteException {
            loopInvokeStart();
        }

        @Override
        public void stop() throws RemoteException {
            loopInvokeStop();
        }

        @Override
        public boolean send(byte[] array) throws RemoteException {
            if (remote != null) {
                return RemoteBinderInvoke.send(remote, array);
            } else {
                loopInvokeSend(array);
            }
            return false;
        }

        private void loopInvokeStart() {
            if (remote != null) {
                RemoteBinderInvoke.start(remote);
            } else {
                new LoopInvoke(handler) {
                    @Override
                    protected boolean invoke() {
                        if (remote != null) {
                            RemoteBinderInvoke.start(remote);
                            return true;
                        }
                        return true;
                    }
                }.start();
            }
        }

        private void loopInvokeStop() {
            if (remote != null) {
                RemoteBinderInvoke.stop(remote);
            } else {
                new LoopInvoke(handler) {
                    @Override
                    protected boolean invoke() {
                        if (remote != null) {
                            RemoteBinderInvoke.stop(remote);
                            return true;
                        }
                        return true;
                    }
                }.start();
            }
        }

        private void loopInvokeSend(final byte[] array) {
            if (remote != null) {
                RemoteBinderInvoke.send(remote, array);
            } else {
                new LoopInvoke(handler) {
                    @Override
                    protected boolean invoke() {
                        if (remote != null) {
                            RemoteBinderInvoke.send(remote, array);
                            return true;
                        }
                        return true;
                    }
                }.start();
            }
        }
    }
}
