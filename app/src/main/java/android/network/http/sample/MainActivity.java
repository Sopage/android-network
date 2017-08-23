package android.network.http.sample;

import android.app.Activity;
import android.network.protocol.protobuf.BodyType;
import android.network.protocol.protobuf.Protobuf;
import android.network.sdk.DreamManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.google.protobuf.InvalidProtocolBufferException;


public class MainActivity extends Activity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btn = (Button) findViewById(R.id.button2);
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                btn.setText((String)msg.obj);
            }
        };
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DreamManager.getSender().login(101, "token");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
