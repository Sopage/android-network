package android.network.http.core;

import android.network.http.request.Request;
import android.network.http.response.ResponseCallback;
import android.os.Handler;
import android.os.Looper;

import java.util.Vector;

/**
 * @author Mr.Huang
 * @date 2017/11/17
 */
public class ThreadPool {

    private static volatile ThreadPool singleton;
    private Thread[] threads;
    private Handler handler;
    private Vector<Request> vector = new Vector<>();

    private ThreadPool() {
        threads = new Thread[3];
        handler = new Handler(Looper.getMainLooper());
    }

    public static ThreadPool getInstance() {
        if (singleton == null) {
            synchronized (ThreadPool.class) {
                if (singleton == null) {
                    singleton = new ThreadPool();
                }
            }
        }
        return singleton;
    }

    public void execute(Request request) {
        vector.add(request);
        for (int i = 0; i < threads.length; i++) {
            Thread thread = threads[i];
            if (thread == null || !thread.isAlive()) {
                thread = new Thread(new DownloadTask());
                threads[i] = thread;
                thread.start();
            }
        }
    }

    private class DownloadTask implements Runnable {
        @Override
        public void run() {
            while (vector.size() > 0){
                Request request = vector.remove(0);
                ResponseCallback callback = request.getResponseCallback();
                switch (request.getMethod()){
                    case Request.METHOD_GET:
//                        Http.get(request.getUrl(), request.getHeaders(), request.getParams());
                        break;
                    case Request.METHOD_POST:
                        break;
                    case Request.METHOD_UPLOAD:
                        break;
                    case Request.METHOD_DOWNLOAD:
                        break;
                }
            }
        }
    }

}
