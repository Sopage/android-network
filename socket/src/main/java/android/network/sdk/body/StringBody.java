package android.network.sdk.body;

import android.network.protocol.Body;

/**
 * @author Mr.Huang
 * @date 2017/12/1
 */
public class StringBody extends Body {

    private String string;

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    @Override
    public byte[] toArray() {
        return string != null ? string.getBytes() : new byte[0];
    }

    @Override
    protected void source(byte[] body) {
        string = new String(body);
    }

    @Override
    protected int getType() {
        return BodyType.STRING;
    }
}
