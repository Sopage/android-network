package android.network.remote.codec;

import android.network.protocol.Packet;
import android.network.protocol.Protocol;

import com.dream.socket.codec.MessageDecode;
import com.dream.socket.codec.MessageEncode;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

/**
 * @author Mr.Huang
 * @date 2017/8/17
 */
public class MessageCodec {

    public MessageDecode<Packet> getDecode() {
        return new MessageDecode<Packet>() {
            @Override
            protected Packet decode(SocketAddress address, ByteBuffer buffer) {
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
        };
    }

    public MessageEncode<Packet> getEncode() {
        return new MessageEncode<Packet>() {
            @Override
            public void encode(Packet data, ByteBuffer buffer) {
                buffer.put(Protocol.START_TAG);
                buffer.put(Protocol.VERSION);
                buffer.putInt(data.getBody().length + Protocol.HEADER_LENGTH);
                buffer.put(Protocol.RETAIN);
                buffer.put(Protocol.VERIFY_TAG);
                buffer.put(data.getBody());
                buffer.put(Protocol.END_TAG);
            }
        };
    }

}
