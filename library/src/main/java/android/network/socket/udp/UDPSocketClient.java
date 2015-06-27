package android.network.socket.udp;

import android.network.socket.Receiver;

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
        System.out.println("UDP RECEIVE OVER");
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
            System.out.println("UDP SEND OVER");
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

//    public static void main(String[] args) throws Exception {
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    DatagramSocket server = new DatagramSocket(new InetSocketAddress("127.0.0.1", 9999));
//                    byte[] recvBuf = new byte[1024 * 3];
//                    DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
//                    while (true) {
//                        server.receive(recvPacket);
//                        String recvStr = new String(recvPacket.getData(), 0, recvPacket.getLength());
//                        String sendStr = "Hello ! I'm Server - " + recvStr;
//                        byte[] sendBuf = sendStr.getBytes();
//                        DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, recvPacket.getSocketAddress());
//                        server.send(sendPacket);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//        UDPSocketClient client = new UDPSocketClient("127.0.0.1", 9999, new Receiver() {
//            @Override
//            public void receive(byte[] buffer) {
//                System.out.println(new String(buffer));
//            }
//        });
//        new Thread(client).start();
//        int i = 1;
//        while (true) {
//            Thread.sleep(1000);
//            if(i == 10){
//                client.close();
//                return;
//            }
//            client.send(("Your Name Client " + i).getBytes());
//            i++;
//        }
//    }
}
