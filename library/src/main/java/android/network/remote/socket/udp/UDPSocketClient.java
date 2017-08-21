package android.network.remote.socket.udp;

import android.network.remote.socket.Receiver;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
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

    @Override
    public void run() {
        try {
            mSocket = new SSocket(receiver);
            sendThread = new SendThread(mSocket);
            sendThread.setAddress(address);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (isConnect) {
            try {
                mSocket.receive();
            } catch (Exception e) {
            }
        }
        sendThread.close();
        mSocket.close();
    }

    public void send(byte[] data) {
        sendThread.send(data);
    }

    public void close() {
        isConnect = false;
        sendThread.close();
        mSocket.close();
    }

    private static class SendThread extends Thread {

        private static final Vector<byte[]> datas = new Vector<byte[]>();
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
                        } catch (Exception e) {
                            continue;
                        }
                    } else {
                        this.notify();
                    }
                }
            }
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
