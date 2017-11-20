package android.network.http;

/**
 *
 */
public interface HttpProgress {

    void progress(int contentLength, int currentLength);

}
