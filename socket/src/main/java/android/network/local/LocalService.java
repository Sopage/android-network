package android.network.local;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.network.binder.remote.IRemoteBinder;
import android.network.invoke.RemoteBinderInvoke;
import android.network.local.binder.LocalBinder;
import android.network.local.binder.RemoteCallbackBinder;
import android.network.model.Status;
import android.network.remote.RemoteService;
import android.os.IBinder;

/**
 * @author Mr.Huang
 * @date 2017/8/22
 */
public class LocalService extends Service implements ServiceConnection, RemoteCallbackBinder.RemoteCallback, LocalBinder.GetRemoteBinder {

    private IRemoteBinder remote;
    private LocalBinder local = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return local;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent remote = new Intent(this, RemoteService.class);
        bindService(remote, this, Service.BIND_AUTO_CREATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        remote = IRemoteBinder.Stub.asInterface(service);
        RemoteBinderInvoke.register(remote, new RemoteCallbackBinder(this));
        local.setRemoteBinder(this);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        remote = null;
    }

    @Override
    public IRemoteBinder getRemoteBinder() {
        return remote;
    }

    @Override
    public void onStatus(int status) {
        switch (status) {
            case Status.CONNECTED:
                local.login();
                break;
        }
    }

    @Override
    public void onMessage(byte[] body) {
        local.onMessage(body);
    }
}
