package android.network.http.sample;

import android.app.Activity;
import android.network.protocol.Body;
import android.network.protocol.MessageBody;
import android.network.sdk.DreamManager;
import android.network.sdk.body.StringBody;
import android.network.sdk.listener.OnReceiverMessage;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements OnReceiverMessage{

    public int index = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btn = (Button) findViewById(R.id.button2);
        DreamManager.getReceiver().register(this);
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DreamManager.getSender().login(1, "token");
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DreamManager.getSender().send(new StringBody("message -> " + index));
                index++;
            }
        });
    }

    @Override
    public void onMessage(Body body) {

    }

    @Override
    public void onStatus(int status) {

    }

    @Override
    protected void onDestroy() {
        DreamManager.getReceiver().unregister(this);
        super.onDestroy();
    }
}
