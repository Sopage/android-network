package android.network.remote.codec;

import android.network.protocol.Body;
import android.network.protocol.Protocol;

import com.dream.socket.codec.Message;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

/**
 * @author Mr.Huang
 * @date 2017/8/17
 */
public class MessageCodec implements com.dream.socket.codec.MessageCodec {

    @Override
    public Message decode(SocketAddress address, ByteBuffer buffer) {
        int limit = buffer.limit();
        if (limit < Protocol.HEADER_LENGTH) {
            return null;
        }
        byte start = buffer.get();
        if (start != '<') {
            buffer.clear();
            return null;
        }
        int length = buffer.getInt();
        if (length > limit) {
            return null;
        }
        int type = buffer.getInt();
        buffer.get(Protocol.RETAIN);
        byte[] body = new byte[length - Protocol.HEADER_LENGTH];
        buffer.get(body);
        byte end = buffer.get();
        if (end != '>') {
            buffer.clear();
            return null;
        }
        return new Body().body(type, body);
    }

    @Override
    public void encode(Message message, ByteBuffer buffer) {
//        buffer.put(Protocol.START_TAG);
//        buffer.putInt(data.getBody().length + Protocol.HEADER_LENGTH);
//        buffer.putInt(data.getType());
//        buffer.put(Protocol.RETAIN);
//        buffer.put(data.getBody());
//        buffer.put(Protocol.END_TAG);
    }
}
