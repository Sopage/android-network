package android.network.socket.codec;

import java.nio.ByteBuffer;

/**
 * 编码器类
 * @param <E> 要编码的对象类型
 */
public interface Encode<E> {

    /**
     * 编码器方法
     * @param data 要编码的对象
     * @param buffer 传入的ByteBuffer 建议只对ByteBuffer做put相关操作
     */
    void encode(E data, ByteBuffer buffer);

}
