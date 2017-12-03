package android.network.remote.codec;

import android.network.protocol.Message;
import android.network.protocol.Protocol;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

/**
 * @author Mr.Huang
 * @date 2017/8/17
 */
public class MessageCodec implements com.dream.socket.codec.MessageCodec<Message> {

    //{起始标记   -byte     - 1}
    //{包总长度   -int      - 4}
    //{包的类型   -int      - 4}
    //{发送者ID   -int      - 4}
    //{接受者ID   -int      - 4}
    //{消息ID    -byte[32] - 32}
    //{包体内容   -byte[n]  - n}
    //{结束标记   -byte     - 1}
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
        int sender = buffer.getInt();
        int recipient = buffer.getInt();
        byte[] idBytes = new byte[Protocol.ID_LENGTH];
        buffer.get(idBytes);
        byte[] bodyBytes = new byte[length - Protocol.HEADER_LENGTH];
        buffer.get(bodyBytes);
        byte end = buffer.get();
        if (end != '>') {
            buffer.clear();
            return null;
        }
        return new Message(new String(idBytes), type, sender, recipient, bodyBytes);
    }

    //{起始标记   -byte     - 1}
    //{包总长度   -int      - 4}
    //{包的类型   -int      - 4}
    //{发送者ID   -int      - 4}
    //{接受者ID   -int      - 4}
    //{消息ID    -byte[32] - 32}
    //{包体内容   -byte[n]  - n}
    //{结束标记   -byte     - 1}
    @Override
    public void encode(Message message, ByteBuffer buffer) {
        byte[] body = message.getBodyArray();
        buffer.put(Protocol.START_TAG);
        buffer.putInt(body.length + Protocol.HEADER_LENGTH);
        buffer.putInt(message.getType());
        buffer.putInt(message.getSender());
        buffer.putInt(message.getRecipient());
        buffer.put(id(message.getId()));
        buffer.put(body);
        buffer.put(Protocol.END_TAG);
    }

    private static byte[] id(String id) {
        if (id != null) {
            byte[] bytes = id.getBytes();
            if (bytes.length == Protocol.ID_LENGTH) {
                return bytes;
            }
            byte[] idBytes = new byte[Protocol.ID_LENGTH];
            if (bytes.length > Protocol.ID_LENGTH) {
                System.arraycopy(bytes, 0, idBytes, 0, idBytes.length);
            } else {
                System.arraycopy(bytes, 0, idBytes, 0, bytes.length);
            }
            return idBytes;
        }
        return new byte[Protocol.ID_LENGTH];
    }
}
