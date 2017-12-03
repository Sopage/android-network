package android.network.protocol;

/**
 *
 */
public abstract class Body {

    protected abstract int getType();

    protected abstract byte[] toArray();

    protected abstract void source(byte[] body);
}
