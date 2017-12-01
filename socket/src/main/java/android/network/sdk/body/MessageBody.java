package android.network.sdk.body;

import android.network.protocol.Body;

/**
 *
 */
public abstract class MessageBody extends Body {

    public MessageBody(Body src) {
        super(src);
    }

    public MessageBody(int type) {
        super(type);
    }

    public MessageBody(int type, int recipient) {
        super(type, recipient);
    }

    public MessageBody(int type, int sender, int recipient) {
        super(type, sender, recipient);
    }

    public MessageBody(byte[] id, int type, int sender, int recipient, byte[] body) {
        super(id, type, sender, recipient, body);
    }

    public abstract byte[] getBytes();

    @Override
    public final byte[] getBody() {
        return getBytes();
    }
}
