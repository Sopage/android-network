package android.network;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.network.binder.CallbackBinder;
import android.network.binder.DataBinder;
import android.network.binder.RemoteBinderInvoke;
import android.network.remote.ConnectService;
import android.network.remote.binder.IRemoteBinder;
import android.os.IBinder;
import android.util.Log;

/**
 * @author Mr.Huang
 * @date 2017/8/21
 */
public class DataService extends Service implements ServiceConnection {

    private IRemoteBinder remote;
    private CallbackBinder cb;
    private DataBinder binder = new DataBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent service = new Intent(this, ConnectService.class);
        bindService(service, this, Service.BIND_AUTO_CREATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        remote = IRemoteBinder.Stub.asInterface(service);
        cb = new CallbackBinder(binder);
        if (RemoteBinderInvoke.register(remote, cb)) {
            binder.setRemoteBinder(remote);
        }
        Log.e("ESA", "DataService onServiceConnected");
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        RemoteBinderInvoke.unregister(remote, cb);
        Log.e("ESA", "DataService onServiceDisconnected");
        remote = null;
    }


}
