package android.network.socket.protobuf;

public enum BodyType {

    UNKNOWN(-1),
    ACK(0),//应答
    MESSAGE(10),//消息
    LOGIN(100),//登录
    LOGOUT(101);//退出

    private int type;

    BodyType(int type) {
        this.type = type;
    }

    public int getTypeCode() {
        return type;
    }

    public static BodyType getType(int type) {
        switch (type) {
            case 0:
                return ACK;
            case 10:
                return MESSAGE;
            case 100:
                return LOGIN;
            case 101:
                return LOGOUT;
            default:
                return UNKNOWN;
        }
    }

}
