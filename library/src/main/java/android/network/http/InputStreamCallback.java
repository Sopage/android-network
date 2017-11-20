package android.network.http;

import java.io.InputStream;

/**
 * @author Mr.Huang
 * @date 2017/11/17
 */
public interface InputStreamCallback {

    void stream(InputStream stream, int contentLength);

}
