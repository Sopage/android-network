package android.network.http.core;

/**
 *
 */
public interface Progress {

    void progress(int contentLength, int currentLength);

}
