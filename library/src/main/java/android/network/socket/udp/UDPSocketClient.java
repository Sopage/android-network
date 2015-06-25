package android.network.socket.udp;

import android.network.socket.Receiver;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Vector;

public class UDPSocketClient implements Runnable {
    private boolean isConnect = true;
    private SSocket mSocket;
    private SendThread sendThread;
    private Receiver receiver;
    private SocketAddress address;

    public UDPSocketClient(String host, int port, Receiver receiver) {
        this.address = new InetSocketAddress(host, port);
        this.receiver = receiver;
    }

    public UDPSocketClient(SocketAddress address, Receiver receiver) {
        this.address = address;
        this.receiver = receiver;
    }

    @Override
    public void run() {
        try {
            mSocket = new SSocket(receiver);
            sendThread = new SendThread(mSocket);
            sendThread.setAddress(address);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        while (isConnect) {
            try {
                mSocket.receive();
            } catch (IOException e) {
            }
        }
        sendThread.close();
        mSocket.close();
        Log.e("ESA", "UDP receive OVER");
    }

    public void send(byte[] data) {
        sendThread.send(data);
    }

    public void stop() {
        isConnect = false;
        sendThread.close();
        mSocket.close();
    }

    private static class SendThread extends Thread {

        private final Vector<byte[]> datas = new Vector<byte[]>();
        private boolean isSend = true;
        private SSocket mSocket;
        private SocketAddress address;

        private SendThread(SSocket socket) {
            this.mSocket = socket;
            this.start();
        }

        @Override
        public void run() {
            while (isSend) {
                synchronized (this) {
                    if (datas.size() < 1) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                        }
                        continue;
                    }
                }
                while (datas.size() > 0) {
                    byte[] buffer = datas.remove(0);
                    if (isSend) {
                        try {
                            mSocket.send(address, buffer);
                        } catch (IOException e) {
                            continue;
                        }
                    } else {
                        this.notify();
                    }
                }
            }
            Log.e("ESA", "UDP send OVER");
        }

        public synchronized void close() {
            isSend = false;
            this.notify();
        }

        public void setAddress(SocketAddress address) {
            this.address = address;
        }

        public synchronized void send(byte[] data) {
            datas.add(data);
            this.notify();
        }
    }
}
