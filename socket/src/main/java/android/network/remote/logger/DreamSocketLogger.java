package android.network.remote.logger;

import android.util.Log;

import com.dream.socket.logger.Logger;

/**
 * @author Mr.Huang
 * @date 2017/8/23
 */
public class DreamSocketLogger implements Logger{
    @Override
    public void debug(String log) {
        Log.d("ESA", log);
    }

    @Override
    public void info(String log) {
        Log.i("ESA", log);
    }

    @Override
    public void warn(String log) {
        Log.w("ESA", log);
    }

    @Override
    public void warn(String log, Throwable throwable) {
        Log.w("ESA", log);
    }

    @Override
    public void error(String log) {
        Log.e("ESA", log);
    }

    @Override
    public void error(String log, Throwable throwable) {
        Log.e("ESA", log);
    }
}
