package android.network.socket.codec;

import java.nio.ByteBuffer;

public interface Decode<T> {

    T decode(ByteBuffer buffer);

}
