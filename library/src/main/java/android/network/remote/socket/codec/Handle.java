package android.network.remote.socket.codec;

/**
 * 消息处理类
 * 解码后的消息会回调onReceive方法
 * @param <D>
 */
public interface Handle<D> {

    /**
     * 连接成功
     */
    int STATUS_CONNECTED = 0;

    /**
     * 断开连接
     */
    int STATUS_DISCONNECT = 1;

    /**
     * 连接失败
     */
    int STATUS_FAIL = 2;

    /**
     * 连接状态回调
     * @param status 状态
     */
    void onStatus(int status);

    /**
     * 消息回调
     * 解码后的消息会回调onReceive方法
     * 请在这里做业务相关的处理，建议单独开个线程处理
     * @param data 消息
     */
    void onReceive(D data);

}
