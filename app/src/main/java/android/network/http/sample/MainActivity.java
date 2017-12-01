package android.network.http.sample;

import android.app.Activity;
import android.network.protocol.MessageBody;
import android.network.sdk.DreamManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    public int index = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btn = (Button) findViewById(R.id.button2);
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                btn.setText((String) msg.obj);
            }
        };
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DreamManager.getSender().login(1, "token");
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DreamManager.getSender().send(new TextBody("message -> " + index));
                index++;
            }
        });
    }

    public static final class TextBody extends MessageBody {

        private String text;

        public TextBody(String text) {
            this.text = text;
        }

        @Override
        public byte[] getBytes() {
            return text.getBytes();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
