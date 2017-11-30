package android.network.protocol;

/**
 *
 */
public abstract class MessageBody extends Body {

    public abstract byte[] getBytes();

    @Override
    public final byte[] getBody() {
        return getBytes();
    }
}
