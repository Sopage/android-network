package android.network.http.sample;

import android.app.Activity;
import android.network.protocol.Message;
import android.network.sdk.DreamManager;
import android.network.sdk.body.BodyType;
import android.network.sdk.body.StringBody;
import android.network.sdk.listener.OnReceiverMessage;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener, OnReceiverMessage {

    public int index = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DreamManager.getReceiver().register(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_logout).setOnClickListener(this);
        findViewById(R.id.btn_send).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_login) {
            DreamManager.getSender().login(1, "token");
        } else if (id == R.id.btn_logout) {
            DreamManager.getSender().logout();
        } else if (id == R.id.btn_send) {
            Message message = new Message(BodyType.STRING, 123456, new StringBody("message -> " + index));
            DreamManager.getSender().send(message);
            index++;
        }
    }

    @Override
    public void onMessage(Message message) {
        Log.e("ESA", message.getBody().toString());
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
