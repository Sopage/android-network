package android.network.listener;

/**
 * @author Mr.Huang
 * @date 2017/8/23
 */
public interface OnReceiverMessage {

    void onMessage(int sender, int type, String text);

}
