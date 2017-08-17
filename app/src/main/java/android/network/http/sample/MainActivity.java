package android.network.http.sample;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.network.binder.TextMessage;
import android.network.socket.CoreService;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;


public class MainActivity extends Activity {
    android.network.binder.IBinder binder;
    final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ServiceConnection sc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = android.network.binder.IBinder.Stub.asInterface(service);
                try {
                    binder.login(1, "toke");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                binder.send(new TextMessage());
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 3000);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        Intent intent = new Intent(this, CoreService.class);
        bindService(intent, sc, Service.BIND_AUTO_CREATE);
    }
}
