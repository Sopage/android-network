package android.network.socket.tcp;

import android.network.socket.Receiver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class SSocket {

    private Socket mSocket;
    private OutputStream out;
    private InputStream in;
    private Receiver receiver;

    public SSocket(Receiver receiver) {
        this.receiver = receiver;
    }

    public void connect(String host, int port) throws Exception {
        mSocket = new Socket();
        mSocket.connect(new InetSocketAddress(host, port), 1000 * 10);
        if (mSocket.isConnected()) {
            out = mSocket.getOutputStream();
            in = mSocket.getInputStream();
            receiver.connected();
        }
    }

    public void write(byte[] buffer) throws IOException {
        if (out != null) {
            out.write(buffer);
            out.flush();
        }
    }

    public void disconnect() {
        if (mSocket == null) {
            return;
        }
        if (!mSocket.isInputShutdown()) {
            try {
                mSocket.shutdownInput();
            } catch (IOException e) {
            }
        }
        if (!mSocket.isOutputShutdown()) {
            try {
                mSocket.shutdownOutput();
            } catch (IOException e) {
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
            }
        }
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
        if (mSocket.isConnected() || !mSocket.isClosed()) {
            try {
                mSocket.close();
            } catch (IOException e) {
            }
        }
        receiver.disconnect();
        out = null;
        in = null;
        mSocket = null;
    }

    public void read() throws IOException {
        if (in != null) {
            byte[] buffer = new byte[1024 * 3];
            byte[] tmpBuffer;
            int len;
            while ((len = in.read(buffer)) > 0) {
                tmpBuffer = new byte[len];
                System.arraycopy(buffer, 0, tmpBuffer, 0, len);
                receiver.receive(tmpBuffer);
            }
        }
    }

}
