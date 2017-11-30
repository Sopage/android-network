package android.network.remote.codec;

import android.network.protocol.Body;
import android.network.protocol.Protocol;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

/**
 * @author Mr.Huang
 * @date 2017/8/17
 */
public class MessageCodec implements com.dream.socket.codec.MessageCodec<Body> {

    @Override
    public Body decode(SocketAddress address, ByteBuffer buffer) {
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
        return new Body(type, body);
    }

    @Override
    public void encode(Body message, ByteBuffer buffer) {
        byte[] body = message.getBody();
        buffer.put(Protocol.START_TAG);
        buffer.putInt(body.length + Protocol.HEADER_LENGTH);
        buffer.putInt(message.getType());
        buffer.put(Protocol.RETAIN);
        buffer.put(body);
        buffer.put(Protocol.END_TAG);
    }
}
