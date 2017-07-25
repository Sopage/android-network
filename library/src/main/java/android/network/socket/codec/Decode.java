package android.network.socket.codec;

import java.nio.ByteBuffer;

public interface Decode<D> {

    D decode(ByteBuffer buffer);

}
