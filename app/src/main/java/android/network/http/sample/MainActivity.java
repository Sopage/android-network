package android.network.http.sample;

import android.network.socket.Receiver;
import android.network.socket.tcp.TCPSocketClient;
import android.network.socket.udp.UDPSocketClient;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;


public class MainActivity extends ActionBarActivity {

    private TCPSocketClient tcpSocketClient;
    private UDPSocketClient udpSocketClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tcpSocketClient = new TCPSocketClient("10.0.2.2", 9999, new Receiver() {
            @Override
            public void connected() {

            }

            @Override
            public void receive(byte[] buffer) {
                Log.e("ESA", "Server TCP -> " + new String(buffer));
            }

            @Override
            public void disconnect() {

            }
        });
        new Thread(tcpSocketClient).start();
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tcpSocketClient.write("hello world!".getBytes());
            }
        });

        udpSocketClient = new UDPSocketClient("10.0.2.2", 9999, new Receiver() {
            @Override
            public void connected() {

            }

            @Override
            public void receive(byte[] buffer) {
                Log.e("ESA", "Server UDP -> " + new String(buffer));
            }

            @Override
            public void disconnect() {

            }
        });
        new Thread(udpSocketClient).start();
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                udpSocketClient.send("hello world!".getBytes());
            }
        });
    }

    @Override
    public void onBackPressed() {
        udpSocketClient.close();
        tcpSocketClient.close();
        super.onBackPressed();
    }
}
