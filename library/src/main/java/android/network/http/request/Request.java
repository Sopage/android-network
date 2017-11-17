package android.network.http.request;

import android.network.http.core.ThreadPool;
import android.network.http.response.ResponseCallback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr.Huang
 * @date 2017/11/17
 */
public class Request {

    public static final int METHOD_GET = 1;
    public static final int METHOD_POST = 2;
    public static final int METHOD_UPLOAD = 3;
    public static final int METHOD_DOWNLOAD = 4;

    private String url;
    private int method;
    private Map<String, String> params = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();
    private Map<String, File[]> files = new HashMap<>();
    private ResponseCallback callback;

    private Request(String url) {
        this.url = url;
    }

    public void doRequest(){
        ThreadPool.getInstance().execute(this);
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public void addParam(String key, String value) {
        params.put(key, value);
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void addFile(String key, File file) {
        File[] array = files.get(key);
        if (array == null) {
            array = new File[]{file};
            files.put(key, array);
        } else {
            File[] newArray = new File[array.length + 1];
            int i = 0;
            for (File f : array) {
                newArray[i] = f;
                i++;
            }
            newArray[i] = file;
            files.put(key, newArray);
        }
    }

    public void setOnResponseCallback(ResponseCallback callback) {
        this.callback = callback;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public Map<String, File[]> getFiles() {
        return files;
    }

    public ResponseCallback getResponseCallback(){
        return callback;
    }

    public int getMethod() {
        return method;
    }
}
