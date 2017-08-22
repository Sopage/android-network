package android.network.invoke;

import android.os.Handler;

/**
 * @author Mr.Huang
 * @date 2017/8/22
 */
public abstract class LoopInvoke implements Runnable {

    private static final int LOOP_TIME = 100;
    private Handler handler;
    private int num;

    public LoopInvoke(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        if (num < 100 && !invoke() && handler != null) {
            handler.postDelayed(this, LOOP_TIME);
        }
        num++;
    }

    public boolean start() {
        if (handler != null) {
            handler.postDelayed(this, LOOP_TIME);
            return true;
        }
        return false;
    }

    protected abstract boolean invoke();
}
