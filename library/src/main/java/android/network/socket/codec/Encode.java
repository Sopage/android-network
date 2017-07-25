package android.network.socket.codec;

import java.nio.ByteBuffer;

public interface Encode<E> {

    void encode(E data, ByteBuffer buffer);

}
