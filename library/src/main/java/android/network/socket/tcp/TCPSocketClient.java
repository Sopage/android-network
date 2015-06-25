package android.network.socket.tcp;

import android.network.socket.Receiver;
import android.util.Log;

import java.io.IOException;
import java.util.Vector;

public class TCPSocketClient implements Runnable {

    private boolean isConnect = true;
    private String host;
    private int port;
    private SSocket mSocket;
    private WriteThread mWriteThread;
    private Receiver mReceiver;

    public TCPSocketClient(String host, int port, Receiver receiver) {
        this.host = host;
        this.port = port;
        this.mReceiver = receiver;
        mSocket = new SSocket(mReceiver);
    }

    @Override
    public void run() {
        if (mReceiver == null) {
            return;
        }
        while (isConnect) {
            synchronized (this) {
                try {
                    mSocket.connect(host, port);
                } catch (Exception e) {
                    try {
                        this.wait(6000);
                    } catch (InterruptedException e1) {
                    }
                    continue;
                }
            }
            try {
                mWriteThread = new WriteThread(mSocket);
                mSocket.read();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mWriteThread.close();
                mSocket.disconnect();
            }
        }
        Log.e("ESA", "TCP read over");
    }

    public void write(byte[] buffer) {
        if (mWriteThread != null) {
            mWriteThread.write(buffer);
        }
    }

    public synchronized void stop() {
        isConnect = false;
        mWriteThread.close();
        mSocket.disconnect();
        this.notify();
    }

    private static class WriteThread extends Thread {

        private boolean isWrite = true;
        private final Vector<byte[]> datas = new Vector<byte[]>();
        private SSocket mSocket;

        private WriteThread(SSocket socket) {
            this.mSocket = socket;
            this.start();
        }


        @Override
        public void run() {
            while (isWrite) {
                synchronized (this) {
                    if (datas.size() <= 0) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                        }
                        continue;
                    }
                }
                while (datas.size() > 0) {
                    try {
                        byte[] buffer = datas.remove(0);
                        if (isWrite) {
                            mSocket.write(buffer);
                        } else {
                            this.notify();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
            Log.e("ESA", "TCP write over");
        }

        public synchronized void write(byte[] buffer) {
            datas.add(buffer);
            this.notify();
        }

        public synchronized void close() {
            isWrite = false;
            this.notify();
        }
    }
}
