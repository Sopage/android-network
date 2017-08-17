package android.network.http.sample.protocol;

public class Packet extends Protocol {

    /**
     * 当前消息内容
     */
    public byte[] body;

    public Packet(){

    }

    public Packet(byte[] body){
        this.body = body;
    }

}
