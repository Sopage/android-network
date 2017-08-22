package android.network.http.sample;

import android.app.Activity;
import android.network.listener.OnMessageListener;
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
                DreamManager.getReceiver().addOnMessageListener(new OnMessageListener() {
                    @Override
                    public void onMessage(byte[] array) {
                        try {
                            Protobuf.Body mBody = Protobuf.Body.parseFrom(array);
                            BodyType type = BodyType.getType(mBody.getType());
                            switch (type){
                                case ACK:
                                    break;
                                case MESSAGE:
                                    Protobuf.Message message = Protobuf.Message.parseFrom(mBody.getContent());
                                    handler.obtainMessage(0, message.getContent().toStringUtf8()).sendToTarget();
                                    break;
                            }
                        } catch (InvalidProtocolBufferException e) {
                            e.printStackTrace();
                        }
                    }
                });
                DreamManager.getSender().login(101, "200");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
