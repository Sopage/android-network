package android.network.http.sample;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.network.binder.remote.IRemoteBinder;
import android.network.listener.OnReceiverMessage;
import android.network.protocol.Body;
import android.network.remote.RemoteService;
import android.network.sdk.DreamManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    public int index = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, RemoteService.class);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                IRemoteBinder binder = IRemoteBinder.Stub.asInterface(iBinder);
                try {
                    binder.send(new TextBody("hello"));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        }, Service.BIND_AUTO_CREATE);
        final Button btn = (Button) findViewById(R.id.button2);
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                btn.setText((String) msg.obj);
            }
        };
//        DreamManager.getReceiver().addOnReceiverMessage(new OnReceiverMessage() {
//            @Override
//            public void onMessage(int sender, int type, String text) {
//                handler.obtainMessage(1, text).sendToTarget();
//            }
//        });
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index++;
            }
        });
    }

    public static final class TextBody extends Body implements android.os.Parcelable {

        private String text;

        public TextBody(String text){
            this.text = text;
        }
        protected TextBody(Parcel in) {
            text = in.readString();
        }

        public static final Creator<TextBody> CREATOR = new Creator<TextBody>() {
            @Override
            public TextBody createFromParcel(Parcel in) {
                return new TextBody(in);
            }

            @Override
            public TextBody[] newArray(int size) {
                return new TextBody[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(text);
        }

        @Override
        public String toString() {
            return text;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
