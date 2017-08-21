package android.network.protocol.protobuf;

public enum MessageType {
    UNKNOWN(-1),
    SINGLE(1),//单向消息(单聊消息)
    GROUP(10),//多向消息(群消息)
    PUSH(100);//推送消息

    private int type;

    MessageType(int type) {
        this.type = type;
    }

    public int getTypeCode() {
        return type;
    }

    public static MessageType getType(int type) {
        switch (type) {
            case 1:
                return SINGLE;
            case 10:
                return GROUP;
            case 100:
                return PUSH;
            default:
                return UNKNOWN;
        }
    }
}
