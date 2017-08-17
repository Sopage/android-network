package android.network.http.sample.protobuf;

/**
 * 消息类型
 */
public interface Type {

    /**
     * 应答
     */
    byte BODY_ACK = 0;

    /**
     * 登录
     */
    byte BODY_MESSAGE = 10;

    /**
     * 登录
     */
    byte BODY_LOGIN = 100;

    /**
     * 退出
     */
    byte BODY_LOGOUT = 101;

    /**
     * 推送消息
     */
    byte BODY_PUSH = 100;

    /**
     * 单向消息(单聊消息)
     */
    byte MESSAGE_SINGLE = 1;

    /**
     * 多向消息(群消息)
     */
    byte MESSAGE_GROUP = 10;

}
