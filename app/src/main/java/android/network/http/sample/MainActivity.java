package android.network.http.sample;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.network.DataService;
import android.network.binder.DataBinder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


public class MainActivity extends Activity {

    private ServiceConnection connection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent service = new Intent(this, DataService.class);
        startService(service);
        bindService(service, connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                DataBinder binder = (DataBinder) service;
                binder.login(100, "token");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.e("ESA", "-----------onServiceDisconnected-------------");

            }
        }, Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
