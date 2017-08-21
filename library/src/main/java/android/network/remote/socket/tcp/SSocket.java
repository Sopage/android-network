package android.network.remote.socket.tcp;

import android.network.remote.socket.Receiver;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SSocket {

    private Socket mSocket;
    private OutputStream out;
    private InputStream in;

    public SSocket() {
    }

    public void connect(String host, int port) throws Exception {
        mSocket = new Socket();
        mSocket.connect(new InetSocketAddress(host, port), 1000 * 10);
        if (mSocket.isConnected()) {
            out = mSocket.getOutputStream();
            in = mSocket.getInputStream();
        }
    }

    public void write(byte[] buffer) throws Exception {
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
            } catch (Exception e) {
            }
        }
        if (!mSocket.isOutputShutdown()) {
            try {
                mSocket.shutdownOutput();
            } catch (Exception e) {
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (Exception e) {
            }
        }
        if (in != null) {
            try {
                in.close();
            } catch (Exception e) {
            }
        }
        if (mSocket.isConnected() || !mSocket.isClosed()) {
            try {
                mSocket.close();
            } catch (Exception e) {
            }
        }
        out = null;
        in = null;
        mSocket = null;
    }

    public void read(Receiver receiver) throws Exception {
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
