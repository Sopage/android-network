package android.network.protocol;

/**
 *
 */
public class TextBody extends Body {

    public String getText() {
        return new String(getBody());
    }
}
