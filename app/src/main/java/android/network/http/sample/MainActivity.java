package android.network.http.sample;

import android.app.Activity;
import android.network.protocol.Body;
import android.network.protocol.Message;
import android.network.sdk.DreamManager;
import android.network.sdk.body.StringBody;
import android.network.sdk.listener.OnReceiverMessage;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener, OnReceiverMessage {

    public int index = 1;
    private TextView tv_message;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DreamManager.getReceiver().register(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_logout).setOnClickListener(this);
        findViewById(R.id.btn_send).setOnClickListener(this);
        tv_message = findViewById(R.id.tv_message);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_login) {
            DreamManager.getSender().login(1, "token");
        } else if (id == R.id.btn_logout) {
            DreamManager.getSender().logout();
        } else if (id == R.id.btn_send) {
            StringBody body = new StringBody();
            body.setString("message -> " + index);
            Message message = new Message(100000, 200000, body);
            DreamManager.getSender().send(message);
            index++;
        }
    }

    @Override
    public void onMessage(Message message) {
        Body body = message.getBody();
        if(body instanceof StringBody){
            tv_message.append(((StringBody) body).getString());
            tv_message.append("\n");
        }
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
