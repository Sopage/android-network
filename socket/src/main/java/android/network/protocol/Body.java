package android.network.protocol;

import com.dream.socket.codec.Message;

public class Body extends Message implements Protocol {

    private int type;
    private byte[] body;

    public Body body(int type, byte[] body) {
        this.type = type;
        this.body = body;
        return this;
    }

    public byte[] getBody() {
        if(body == null){
            return new byte[0];
        }
        return body;
    }

    public int getType() {
        return type;
    }
}
