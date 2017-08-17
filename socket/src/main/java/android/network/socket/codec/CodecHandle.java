package android.network.socket.codec;

import android.network.socket.binder.ServiceBinder;
import android.network.socket.protocol.Packet;
import android.network.socket.protocol.Protocol;

import com.dream.socket.DreamSocket;
import com.dream.socket.codec.Codec;
import com.dream.socket.codec.Decode;
import com.dream.socket.codec.Encode;
import com.dream.socket.codec.Handle;

import java.nio.ByteBuffer;

/**
 * @author Mr.Huang
 * @date 2017/8/17
 */

public class CodecHandle extends Codec<Packet, Packet> implements Handle<Packet>, Decode<Packet>, Encode<Packet> {

    private DreamSocket socket;
    private ServiceBinder binder;

    public void setSocket(DreamSocket socket) {
        this.socket = socket;
    }

    public void setBinder(ServiceBinder binder) {
        this.binder = binder;
    }

    @Override
    public Decode<Packet> getDecode() {
        return this;
    }

    @Override
    public Encode<Packet> getEncode() {
        return this;
    }

    @Override
    public void onStatus(int status) {
        if (status == Handle.STATUS_CONNECTED) {
            if(binder != null){
                binder.doLogin();
            }
        } else if (status == Handle.STATUS_DISCONNECT) {

        } else if (status == Handle.STATUS_FAIL) {

        }
    }

    @Override
    public void onMessage(Packet data) {

    }

    @Override
    public Packet decode(ByteBuffer buffer) {
        int limit = buffer.limit();
        if (limit < Protocol.HEADER_LENGTH) {
            return null;
        }
        char start = (char) buffer.get();
        byte version = buffer.get();
        int length = buffer.getInt();//包的总长度 包括头
        buffer.get(Protocol.RETAIN);
        char xy = (char) buffer.get();
        if (limit < length) {
            return null;
        }
        byte[] bytes = new byte[length - Protocol.HEADER_LENGTH];
        buffer.get(bytes);
        char end = (char) buffer.get();
        return new Packet(bytes);
    }

    @Override
    public void encode(Packet packet, ByteBuffer buffer) {
        buffer.put(Protocol.START_TAG);
        buffer.put(Protocol.VERSION);
        buffer.putInt(packet.body.length + Protocol.HEADER_LENGTH);
        buffer.put(Protocol.RETAIN);
        buffer.put(Protocol.VERIFY_TAG);
        buffer.put(packet.body);
        buffer.put(Protocol.END_TAG);
    }
}
