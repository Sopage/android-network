package android.network.protocol;

public class Packet extends Protocol {

    /**
     * 当前消息内容
     */
    private byte[] body;

    public Packet(){

    }

    public Packet(byte[] body){
        this.body = body;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
