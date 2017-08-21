package android.network.listener;

/**
 * @author Mr.Huang
 * @date 2017/8/21
 */
public interface OnStatusListener {

    /**
     * @see android.network.model.Status
     * @param status
     */
    void onStatus(int status);

}
