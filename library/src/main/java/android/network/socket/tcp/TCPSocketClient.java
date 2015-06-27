package android.network.socket.tcp;

import android.network.socket.Receiver;

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
        System.out.println("TCP READ END");
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
            System.out.println("TCP WRITE END");
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

    static TCPSocketClient client;

//    public static void main(String[] args) throws Exception {
//        final StringBuilder sb = new StringBuilder("GET / HTTP/1.1\r\n");
//        sb.append("Connection: Keep-Alive").append("\r\n");
//        sb.append("Accept: */*").append("\r\n");
//        sb.append("Accept-Charset: UTF-8").append("\r\n");
//        sb.append("Accept-Language: zh-CN").append("\r\n");
//        sb.append("Host: www.baidu.com").append("\r\n");
//        sb.append("\r\n").append("\r\n");
//        System.out.println("init");
//        client = new TCPSocketClient("www.baidu.com", 80, new Receiver() {
//            @Override
//            public void receive(byte[] buffer) {
//                System.out.println("receive");
//                String s = new String(buffer);
//                System.out.println(s);
//                client.close();
//            }
//
//            @Override
//            public void connected() {
//                System.out.println("connected");
//                client.write(sb.toString().getBytes());
//            }
//
//            @Override
//            public void disconnect() {
//                System.out.println("disconnect");
//            }
//        });
//        new Thread(client).start();
//    }
}
