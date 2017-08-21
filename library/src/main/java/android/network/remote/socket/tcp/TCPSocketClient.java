package android.network.remote.socket.tcp;

import android.network.remote.socket.Receiver;

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
        this.mSocket = new SSocket();
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
                mReceiver.connected();
                mSocket.read(mReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mReceiver.disconnect();
                mWriteThread.close();
                mSocket.disconnect();
            }
        }
    }

    public void write(byte[] buffer) {
        if (mWriteThread != null) {
            mWriteThread.write(buffer);
        }
    }

    public synchronized void close() {
        isConnect = false;
        mWriteThread.close();
        mSocket.disconnect();
        this.notify();
    }

    private static class WriteThread extends Thread {

        private boolean isWrite = true;
        private static final Vector<byte[]> datas = new Vector<byte[]>();
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
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
