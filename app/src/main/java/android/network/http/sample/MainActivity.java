package android.network.http.sample;

import android.app.Activity;
import android.network.sdk.DreamManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DreamManager.getSender().login(101, "200");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
