package android.network.socket.codec;

import java.nio.ByteBuffer;

public interface Encode<T> {

    void encode(T object, ByteBuffer buffer);

}
