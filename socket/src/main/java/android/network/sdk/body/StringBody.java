package android.network.sdk.body;

import android.network.protocol.Body;

/**
 * @author Mr.Huang
 * @date 2017/12/1
 */
public class StringBody extends MessageBody {

    private String string;

    public StringBody(Body src){
        super(src);
        this.string = new String(src.getBody());
    }

    public StringBody(String string){
        super(TYPE_STRING);
        this.string = string;
    }

    public String getString() {
        return string;
    }

    @Override
    public byte[] getBytes() {
        if(string != null){
            return string.getBytes();
        }
        return new byte[0];
    }

    @Override
    public String toString() {
        return string;
    }
}
