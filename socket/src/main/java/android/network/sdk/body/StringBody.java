package android.network.sdk.body;

import android.network.protocol.Body;

/**
 * @author Mr.Huang
 * @date 2017/12/1
 */
public class StringBody extends Body {

    private String string;

    public StringBody(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    @Override
    public String toString() {
        return string;
    }

    @Override
    public byte[] toArray() {
        return string != null ? string.getBytes() : new byte[0];
    }
}
